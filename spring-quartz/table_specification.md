*qrtz_job_details：存储每一个已配置的 jobDetail 的详细信息*

| 表字段 | 含义 |
| --- | --- |
| sched_name | 调度名称 |
| job_name | 集群中job的名字,该名字用户自己可以随意定制,无强行要求 |
| job_group | 集群中job的所属组的名字,该名字用户自己随意定制,无强行要求 |
| description | 相关介绍 |
| job_class_name | 集群中个notejob实现类的完全包名,quartz就是根据这个路径到classpath找到该job类 |
| is_durable | 是否持久化,把该属性设置为1，quartz会把job持久化到数据库中 |
| is_nonconcurrent | 是否并发 |
| is_update_data | 是否更新数据 |
| requests_recovery | 是否接受恢复执行，默认为false，设置了RequestsRecovery为true，则该job会被重新执行 |
| job_data | 一个blob字段，存放持久化job对象 |
  
*qrtz_triggers： 保存触发器的基本信息*

| 表字段 | 含义 |
| --- | --- |
| sched_name | 调度名称 |
| trigger_name | 触发器的名字,该名字用户自己可以随意定制,无强行要求 |
| trigger_group | 触发器所属组的名字,该名字用户自己随意定制,无强行要求 |
| job_name | qrtz_job_details表job_name的外键 |
| job_group | qrtz_job_details表job_group的外键 |
| description | 相关介绍 |
| next_fire_time | 上一次触发时间（毫秒） |
| prev_fire_time | 下一次触发时间，默认为-1，意味不会自动触发 |
| priority | 优先级 |
| trigger_state | 当前触发器状态，设置为ACQUIRED,如果设置为WAITING,则job不会触发 （ WAITING:等待 PAUSED:暂停ACQUIRED:正常执行 BLOCKED：阻塞 ERROR：错误） |
| trigger_type | 触发器的类型，使用cron表达式 |
| start_time | 开始时间 |
| end_time | 结束时间 |
| calendar_name | 日程表名称，表qrtz_calendars的calendar_name字段外键 |
| misfire_instr | 措施或者是补偿执行的策略 |
| job_data | 一个blob字段，存放持久化job对象 |

*qrtz_cron_triggers：存储触发器的cron表达式表*

| 表字段 | 含义 |
| --- | --- |
| sched_name | 调度名称 |
| trigger_name | qrtz_triggers表trigger_name的外键 |
| trigger_group | qrtz_triggers表trigger_group的外键 |
| cron_expression | cron表达式 |
| time_zone_id | 时区 |

*qrtz_scheduler_state：存储集群中note实例信息，quartz会定时读取该表的信息判断集群中每个实例的当前状态*

| 表字段 | 含义 |
| --- | --- |
| sched_name | 调度名称 |
| instance_name | 之前配置文件中org.quartz.scheduler.instanceId配置的名字，就会写入该字段 |
| last_checkin_time | 上次检查时间 |
| checkin_interval | 检查间隔时间 |

*qrtz_blob_triggers：Trigger 作为 Blob 类型存储(用于 Quartz 用户用 JDBC 创建他们自己定制的 Trigger 类型，JobStore 并不知道如何存储实例的时候)*

| 表字段 | 含义 |
| --- | --- |
| sched_name | 调度名称 |
| trigger_name | qrtz_triggers表trigger_name的外键 |
| trigger_group | qrtz_triggers表trigger_group的外键 |
| blob_data | 一个blob字段，存放持久化Trigger对象 |

*qrtz_calendars：以 Blob 类型存储存放日历信息， quartz可配置一个日历来指定一个时间范围*

| 表字段 | 含义 |
| --- | --- |
| sched_name | 调度名称 |
| calendar_name | 日历名称 |
| calendar | 一个blob字段，存放持久化calendar对象 |

*qrtz_fired_triggers：存储与已触发的 Trigger 相关的状态信息，以及相联 Job 的执行信息*

| 表字段 | 含义 |
| --- | --- |
| sched_name | 调度名称 |
| entry_id | 调度器实例id |
| trigger_name | qrtz_triggers表trigger_name的外键 |
| trigger_group | qrtz_triggers表trigger_group的外键 |
| instance_name | 调度器实例名 |
| fired_time | 触发的时间 |
| sched_time | 定时器制定的时间 |
| priority | 优先级 |
| state | 状态 |
| job_name | 集群中job的名字,该名字用户自己可以随意定制,无强行要求 |
| job_group | 集群中job的所属组的名字,该名字用户自己随意定制,无强行要求 |
| is_nonconcurrent | 是否并发 |
| requests_recovery | 是否接受恢复执行，默认为false，设置了RequestsRecovery为true，则会被重新执行 |

*qrtz_locks：存储程序的悲观锁的信息(假如使用了悲观锁)*

| 表字段 | 含义 |
| --- | --- |
| sched_name | 调度名称 |
| lock_name | 悲观锁名称 |

*qrtz_paused_trigger_grps：存储已暂停的 Trigger 组的信息*

| 表字段 | 含义 |
| --- | --- |
| sched_name | 调度名称 |
| trigger_group | qrtz_triggers表trigger_group的外键 |

*qrtz_paused_trigger_grps：存储已暂停的 Trigger 组的信息*

| 表字段 | 含义 |
| --- | --- |
| sched_name | 调度名称 |
| trigger_group | qrtz_triggers表trigger_group的外键 |

*qrtz_simple_triggers：存储简单的 Trigger，包括重复次数，间隔，以及已触发的次数*

| 表字段 | 含义 |
| --- | --- |
| sched_name | 调度名称 |
| trigger_name | qrtz_triggers表trigger_ name的外键 |
| trigger_group | qrtz_triggers表trigger_group的外键 |
| repeat_count | 重复的次数统计 |
| repeat_interval | 重复的间隔时间 |
| times_triggered | 已经触发的次数 |

*qrtz_simprop_triggers：存储CalendarIntervalTrigger和DailyTimeIntervalTrigger*

| 表字段 | 含义 |
| --- | --- |
| SCHED_NAME | 调度名称 |
| TRIGGER_NAME | qrtz_triggers表trigger_ name的外键 |
| TRIGGER_GROUP | qrtz_triggers表trigger_group的外键 |
| STR_PROP_1 | String类型的trigger的第一个参数 |
| STR_PROP_2 | String类型的trigger的第二个参数 |
| STR_PROP_3 | String类型的trigger的第三个参数 |
| INT_PROP_1 | int类型的trigger的第一个参数 |
| INT_PROP_2 | int类型的trigger的第二个参数 |
| LONG_PROP_1 | long类型的trigger的第一个参数 |
| LONG_PROP_2 | long类型的trigger的第二个参数 |
| DEC_PROP_1 | decimal类型的trigger的第一个参数 |
| DEC_PROP_2 | decimal类型的trigger的第二个参数 |
| BOOL_PROP_1 | Boolean类型的trigger的第一个参数 |
| BOOL_PROP_2 | Boolean类型的trigger的第二个参数 |
