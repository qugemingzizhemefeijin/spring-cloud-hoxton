redisson是redis官网推荐实现分布式锁的一个第三方类库。其内部完成的功能非常强大，对各种锁都有实现，同时对于使用者来说非常简单，让使用者能够将更多的关注点放在业务逻辑上。此处重点利用Redisson解决单机锁产生的两个问题。

### 非公平锁（重入锁）

```
// 加锁逻辑
// redisson的RedissonLock支持可重入的锁
// org.redisson.RedissonLock.tryLockInnerAsync

// keyName
// KEYS[1] = Collections.singletonList(this.getName()) 传入的锁名称
// leaseTime
// ARGV[1] = this.internalLockLeaseTime watchDog配置的超时时间，默认为30s
// uuid+threadId组合的唯一值
// ARGV[2] = this.getLockName(threadId) 锁HASH结构的重入计数key，这里的lockName指的是uuid和threadId组合的唯一值

-- 不存在该key时
if (redis.call('exists', KEYS[1]) == 0) then 
  -- 新增该锁并且hash中该线程id对应的count置1
  redis.call('hincrby', KEYS[1], ARGV[2], 1); 
  -- 设置过期时间
  redis.call('pexpire', KEYS[1], ARGV[1]); 
  return nil; 
end; 

-- 存在该key 并且 hash中线程id的key也存在
if (redis.call('hexists', KEYS[1], ARGV[2]) == 1) then 
  -- 线程重入次数++
  redis.call('hincrby', KEYS[1], ARGV[2], 1); 
  redis.call('pexpire', KEYS[1], ARGV[1]); 
  return nil; 
end; 
return redis.call('pttl', KEYS[1]);
```

判断该锁是否已经有对应hash表存在：
- *没有对应的hash表*：则`set`该`hash`表中一个`entry`的`key`为锁名称，`value`为`1`，之后设置该`hash`表失效时间为`leaseTime`；
- *存在对应的hash表*：则将该`lockName`的`value`执行`+1`操作，也就是计算进入次数，再设置失效时间`leaseTime`；
- 最后返回这把锁的`ttl`剩余时间。

```
// 解锁逻辑
// org.redisson.RedissonLock.unlockInnerAsync

KEYS[1] = name，传入的锁名称
KEYS[2] = channelName，用于pubSub发布消息的channel名称

ARGV[1] = LockPubSub.UNLOCK_MESSAGE，channel发送消息的类别，此处解锁为0
ARGV[2] = internalLockLeaseTime，watchDog配置的超时时间，默认为30s
ARGV[3] = lockName 这里的lockName指的是uuid和threadId组合的唯一值

-- 不存在key
if (redis.call('hexists', KEYS[1], ARGV[3]) == 0) then 
  return nil;
end;
-- 计数器 -1
local counter = redis.call('hincrby', KEYS[1], ARGV[3], -1); 
if (counter > 0) then 
  -- 过期时间重设
  redis.call('pexpire', KEYS[1], ARGV[2]); 
  return 0; 
else
  -- 删除并发布解锁消息
  redis.call('del', KEYS[1]); 
  redis.call('publish', KEYS[2], ARGV[1]); 
  return 1;
end; 
return nil;
```

解锁过程：
- 如果该锁不存在则返回`nil`；
- 如果该锁存在则将其线程的`hash key`计数器`-1`；
- 计数器`counter>0`，重置下失效时间，返回0；否则，删除该锁，发布解锁消息`unlockMessage`，返回1；

其中`unLock`的时候使用到了`Redis`发布订阅`PubSub`完成消息通知。而订阅的步骤就在`RedissonLock`的加锁入口的`lock`方法里。

```
long threadId = Thread.currentThread().getId();
Long ttl = this.tryAcquire(-1L, leaseTime, unit, threadId);
if (ttl != null) {
    // 订阅
    RFuture<RedissonLockEntry> future = this.subscribe(threadId);
    if (interruptibly) {
        this.commandExecutor.syncSubscriptionInterrupted(future);
    } else {
        this.commandExecutor.syncSubscription(future);
    }
    // 省略
```

当锁被其他线程占用时，通过监听锁的释放通知（在其他线程通过`RedissonLock`释放锁时，会通过发布订阅`pub/sub`功能发起通知），等待锁被其他线程释放，也是为了避免自旋的一种常用效率手段。

```
protected void onMessage(RedissonLockEntry value, Long message) {
    Runnable runnableToExecute;
    if (message.equals(unlockMessage)) {
        // 从监听器队列取监听线程执行监听回调
        runnableToExecute = (Runnable)value.getListeners().poll();
        if (runnableToExecute != null) {
            runnableToExecute.run();
        }
        // getLatch()返回的是Semaphore，信号量，此处是释放信号量
        // 释放信号量后会唤醒等待的entry.getLatch().tryAcquire去再次尝试申请锁
        value.getLatch().release();
    } else if (message.equals(readUnlockMessage)) {
        while(true) {
            runnableToExecute = (Runnable)value.getListeners().poll();
            if (runnableToExecute == null) {
                value.getLatch().release(value.getLatch().getQueueLength());
                break;
            }
            runnableToExecute.run();
        }
    }
}
```

发现一个是默认解锁消息，一个是读锁解锁消息，因为`Redisson`是有提供读写锁的，而读写锁读读情况和读写、写写情况互斥情况不同，我们只看上面的默认解锁消息`unlockMessage`分支。

`LockPubSub`监听最终执行了2件事：
- `runnableToExecute.run()`执行监听回调。
- `value.getLatch().release()`释放信号量。

`Redisson`通过`LockPubSub`监听解锁消息，执行监听回调和释放信号量通知等待线程可以重新抢锁。

```
private <T> RFuture<Long> tryAcquireAsync(long leaseTime, TimeUnit unit, long threadId) {
    if (leaseTime != -1) {
        return tryLockInnerAsync(leaseTime, unit, threadId, RedisCommands.EVAL_LONG);
    }
    RFuture<Long> ttlRemainingFuture = tryLockInnerAsync(commandExecutor.getConnectionManager().getCfg().getLockWatchdogTimeout(), TimeUnit.MILLISECONDS, threadId, RedisCommands.EVAL_LONG);
    // 这一段代码，在执行异步加锁的操作后，加锁成功则根据加锁完成返回的ttl是否过期来确认是否执行一段定时任务。这段定时任务的就是watchDog的核心。
    ttlRemainingFuture.onComplete((ttlRemaining, e) -> {
        if (e != null) {
            return;
        }

        // lock acquired
        if (ttlRemaining == null) {
            scheduleExpirationRenewal(threadId);
        }
    });
    return ttlRemainingFuture;
}
```

`org.redisson.RedissonLock.scheduleExpirationRenewal`锁续约

```
private void scheduleExpirationRenewal(long threadId) {
    ExpirationEntry entry = new ExpirationEntry();
    ExpirationEntry oldEntry = EXPIRATION_RENEWAL_MAP.putIfAbsent(getEntryName(), entry);
    if (oldEntry != null) {
        oldEntry.addThreadId(threadId);
    } else {
        entry.addThreadId(threadId);
        renewExpiration();
    }
}

private void renewExpiration() {
    ExpirationEntry ee = EXPIRATION_RENEWAL_MAP.get(getEntryName());
    if (ee == null) {
        return;
    }
    // io.netty.util.HashedWheelTimer.newTimeout
    // org.redisson.connection.MasterSlaveConnectionManager.timer
    Timeout task = commandExecutor.getConnectionManager().newTimeout(new TimerTask() {
        @Override
        public void run(Timeout timeout) throws Exception {
            ExpirationEntry ent = EXPIRATION_RENEWAL_MAP.get(getEntryName());
            if (ent == null) {
                return;
            }
            Long threadId = ent.getFirstThreadId();
            if (threadId == null) {
                return;
            }
            
            RFuture<Boolean> future = renewExpirationAsync(threadId);
            future.onComplete((res, e) -> {
                if (e != null) {
                    log.error("Can't update lock " + getName() + " expiration", e);
                    return;
                }
                
                if (res) {
                    // reschedule itself
                    renewExpiration();
                }
            });
        }
    }, internalLockLeaseTime / 3, TimeUnit.MILLISECONDS);
    
    ee.setTimeout(task);
}

protected RFuture<Boolean> renewExpirationAsync(long threadId) {
    return evalWriteAsync(getName(), LongCodec.INSTANCE, RedisCommands.EVAL_BOOLEAN,
        "if (redis.call('hexists', KEYS[1], ARGV[2]) == 1) then " +
                "redis.call('pexpire', KEYS[1], ARGV[1]); " +
                "return 1; " +
                "end; " +
                "return 0;",
        Collections.singletonList(getName()),
        internalLockLeaseTime, getLockName(threadId));
}
```

- 添加一个`netty`的`Timeout`回调任务，每（`internalLockLeaseTime / 3`）毫秒执行一次，执行的方法是`renewExpirationAsync`。
- `renewExpirationAsync`重置了锁超时时间，又注册一个监听器，监听回调又执行了`renewExpiration`。

```
// KEYS[1] = name，传入的锁名称
// ARGV[1] = internalLockLeaseTime，watchDog配置的超时时间，默认为30s
// ARGV[2] = 锁HASH结构的重入计数key，这里的lockName指的是uuid和threadId组合的唯一值
if (redis.call('hexists', KEYS[1], ARGV[2]) == 1) then 
    redis.call('pexpire', KEYS[1], ARGV[1]); 
    return 1; 
end; 
return 0;
```

目的是为了某种场景下保证业务不影响，如任务执行超时但未结束，锁已经释放的问题。

当一个线程持有了一把锁，由于并未设置超时时间`leaseTime`，`Redisson`默认配置了`30S`，开启`watchDog`，每`10S`对该锁进行一次续约，维持`30S`的超时时间，直到任务完成再删除锁。

这就是`Redisson`的锁续约，也就是`WatchDog`实现的基本思路。

#### 流程概括

通过整体的介绍，流程简单概括：
- A、B线程争抢一把锁，A获取到后，B阻塞；
- B线程阻塞时并非主动CAS，而是PubSub方式订阅该锁的广播消息；
- A操作完成释放了锁，B线程收到订阅消息通知；
- B被唤醒开始继续抢锁，拿到锁；

### 公平锁

`org.redisson.RedissonFairLock.tryLockInnerAsync`

```
-- lua中的几个参数
KEYS = Arrays.<Object>asList(getName(), threadsQueueName, timeoutSetName)
KEYS[1]: lock_name, 锁名称                   
KEYS[2]: "redisson_lock_queue:{xxx}"  线程队列
KEYS[3]: "redisson_lock_timeout:{xxx}"  线程id对应的超时集合

ARGV =  internalLockLeaseTime, getLockName(threadId), currentTime + threadWaitTime, currentTime
ARGV[1]: "{leaseTime}" 过期时间
ARGV[2]: "{Redisson.UUID}:{threadId}"   
ARGV[3] = 当前时间 + 线程等待时间:（10:00:00） + 5000毫秒 = 10:00:05
ARGV[4] = 当前时间（10:00:00）  部署服务器时间，非redis-server服务器时间
```

公平锁实现的Lua脚本：
```
-- 1.死循环清除过期key
while true do 
  -- 获取头节点
    local firstThreadId2 = redis.call('lindex', KEYS[2], 0);
    -- 首次获取必空跳出循环
  if firstThreadId2 == false then 
    break;
  end;
  -- 清除过期key
  local timeout = tonumber(redis.call('zscore', KEYS[3], firstThreadId2));
  if timeout <= tonumber(ARGV[4]) then
    redis.call('zrem', KEYS[3], firstThreadId2);
    redis.call('lpop', KEYS[2]);
  else
    break;
  end;
end;

-- 2.不存在该锁 && （不存在线程等待队列 || 存在线程等待队列而且第一个节点就是此线程ID)，加锁部分主要逻辑
if (redis.call('exists', KEYS[1]) == 0) and 
  ((redis.call('exists', KEYS[2]) == 0)  or (redis.call('lindex', KEYS[2], 0) == ARGV[2])) then
  -- 弹出队列中线程id元素，删除Zset中该线程id对应的元素
  redis.call('lpop', KEYS[2]);
  redis.call('zrem', KEYS[3], ARGV[2]);
  local keys = redis.call('zrange', KEYS[3], 0, -1);
  -- 遍历zSet所有key，将key的超时时间(score) - 当前时间ms
  for i = 1, #keys, 1 do 
    redis.call('zincrby', KEYS[3], -tonumber(ARGV[3]), keys[i]);
  end;
    -- 加锁设置锁过期时间
  redis.call('hset', KEYS[1], ARGV[2], 1);
  redis.call('pexpire', KEYS[1], ARGV[1]);
  return nil;
end;

-- 3.线程存在，重入判断
if redis.call('hexists', KEYS[1], ARGV[2]) == 1 then
  redis.call('hincrby', KEYS[1], ARGV[2],1);
  redis.call('pexpire', KEYS[1], ARGV[1]);
  return nil;
end;

-- 4.返回当前线程剩余存活时间
local timeout = redis.call('zscore', KEYS[3], ARGV[2]);
    if timeout ~= false then
  -- 过期时间timeout的值在下方设置，此处的减法算出的依旧是当前线程的ttl
  return timeout - tonumber(ARGV[3]) - tonumber(ARGV[4]);
end;

-- 5.尾节点剩余存活时间
local lastThreadId = redis.call('lindex', KEYS[2], -1);
local ttl;
-- 尾节点不空 && 尾节点非当前线程
if lastThreadId ~= false and lastThreadId ~= ARGV[2] then
  -- 计算队尾节点剩余存活时间
  ttl = tonumber(redis.call('zscore', KEYS[3], lastThreadId)) - tonumber(ARGV[4]);
else
  -- 获取lock_name剩余存活时间
  ttl = redis.call('pttl', KEYS[1]);
end;

-- 6.末尾排队
-- zSet 超时时间（score），尾节点ttl + 当前时间 + 5000ms + 当前时间，无则新增，有则更新
-- 线程id放入队列尾部排队，无则插入，有则不再插入
local timeout = ttl + tonumber(ARGV[3]) + tonumber(ARGV[4]);
if redis.call('zadd', KEYS[3], timeout, ARGV[2]) == 1 then
  redis.call('rpush', KEYS[2], ARGV[2]);
end;
return ttl;
```

*公平锁加锁步骤*

通过以上`Lua`，可以发现，`lua`操作的关键结构是列表（`list`）和有序集合（`zSet`）。

其中`list`维护了一个等待的线程队列`redisson_lock_queue:{xxx}`，`zSet`维护了一个线程超时情况的有序集合`redisson_lock_timeout:{xxx}`，尽管`lua`较长，但是可以拆分为6个步骤。

- 队列清理：保证队列中只有未过期的等待线程；
- 首次加锁：hset加锁，pexpire过期时间；
- 重入判断：此处同可重入锁lua；
- 返回ttl
- 计算尾节点ttl：初始值为锁的剩余过期时间；
- 末尾排队：ttl + 2 * currentTime + waitTime是score的默认值计算公式；

如果模拟以下顺序，就会明了redisson公平锁整个加锁流程，假设 t1 10:00:00 < t2 10:00:10 < t3 10:00:20：

t1：当线程1初次获取锁

>- 1.等待队列无头节点，跳出死循环->2。
>- 2.不存在该锁 && 不存在线程等待队列 成立。
>- 2.1 lpop和zerm、zincrby都是无效操作，只有加锁生效，说明是首次加锁，加锁后返回nil。
>- 加锁成功，线程1获取到锁，结束。

t2：线程2尝试获取锁（线程1未释放锁）

>- 1.等待队列无头节点，跳出死循环->2
>- 2.不存在该锁 不成立->3
>- 3.非重入线程 ->4
>- 4.score无值 ->5
>- 5.尾节点为空，设置ttl初始值为lock_name的ttl -> 6
>- 6.按照ttl + waitTime + currentTime + currentTime 来设置zSet超时时间score，并且加入等待队列，线程2为头节点
>- 
>- score = 20S + 5000ms + 10:00:10 + 10:00:10 = 10:00:35 + 10:00:10

t3：线程3尝试获取锁（线程1未释放锁）

>- 1.等待队列有头节点
>-   1.1未过期->2
>- 2.不存在该锁不成立->3
>- 3.非重入线程->4
>- 4.score无值 ->5
>- 5.尾节点不为空 && 尾节点线程为2，非当前线程
>-   5.1取出之前设置的score，减去当前时间：ttl = score - currentTime ->6
>- 6.按照ttl + waitTime + currentTime + currentTime 来设置zSet超时时间score，并且加入等待队列
>- 
>- score = 10S + 5000ms + 10:00:20 + 10:00:20 = 10:00:35 + 10:00:20

如此一来，三个需要抢夺一把锁的线程，完成了一次排队，在`list`中排列他们等待线程id，在`zSet`中存放过期时间（便于排列优先级）。其中返回`ttl`的线程2客户端、线程3客户端将会一直按*一定间隔自旋重复执行该段Lua*，尝试加锁，如此一来便和`AQS`有了异曲同工之处。

而当线程1释放锁之后（这里依旧有通过Pub/Sub发布解锁消息，通知其他线程获取）

10:00:30 线程2尝试获取锁（线程1已释放锁）

>- 1.等待队列有头节点，未过期->2
>- 2.不存在该锁 & 等待队列头节点是当前线程 成立
>-   2.1删除当前线程的队列信息和zSet信息，超时时间为：
>- 线程2 10:00:35 + 10:00:10 - 10:00:30 = 10:00:15
>- 线程3 10:00:35 + 10:00:20 - 10:00:30 = 10:00:25
>- 2.2线程2获取到锁，重新设置过期时间
>- 加锁成功，线程2获取到锁，结束

`Redisson`公平锁的玩法类似于延迟队列的玩法，核心都在`Redis`的`List`和`zSet`结构的搭配，但又借鉴了`AQS`实现，在定时判断头节点上如出一辙（`watchDog`），保证了锁的竞争公平和互斥。并发场景下，`lua`脚本里，`zSet`的`score`很好地解决了顺序插入的问题，排列好优先级。

并且为了防止因异常而退出的线程无法清理，每次请求都会判断头节点的过期情况给予清理，最后释放时通过`CHANNEL`通知订阅线程可以来获取锁，重复一开始的步骤，顺利交接到下一个顺序线程。

### 坑

- 如果指定了`leaseTime`，那么就不会启动`Watchdog`进行自动续期；
- 如果没有指定`leaseTime`，则会启动一个`Watchdog`每隔一段时间就对`redis`中的`key`进行续期。默认的时间间隔是10s；

### 总结

`Redisson`也提供了多机情况下的联锁`MultiLock`，和官方推荐的红锁`RedLock`。

[官网文档](https://github.com/redisson/redisson/wiki/)
