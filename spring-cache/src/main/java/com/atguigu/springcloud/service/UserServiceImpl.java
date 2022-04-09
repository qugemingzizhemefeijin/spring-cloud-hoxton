package com.atguigu.springcloud.service;

import com.atguigu.springcloud.configuration.CacheConstant;
import com.atguigu.springcloud.domain.User;
import com.github.benmanes.caffeine.cache.Cache;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service
public class UserServiceImpl implements IUserService, InitializingBean {

    @Resource
    private Cache<String,Object> cache;

    private User user;

    @Override
    public User getById(long id) {
        String key = CacheConstant.USER + id;
        return (User)cache.get(key, k -> {
            log.info("模拟查询DB数据库，id = {}, key = {}", id, k);
            return user;
        });
    }

    @Override
    public void update(User user) {
        if (user == null) {
            throw new NullPointerException("user not found!");
        } else if(user.getId() != user.getId()) {
            throw new IllegalStateException("id error");
        }

        log.info("update user data");

        String key = CacheConstant.USER + user.getId();

        this.user.setPassword(user.getPassword());
        this.user.setUsername(user.getUsername());

        // 修改本地缓存
        cache.put(key, user);
    }

    @Override
    public void delete(long id) {
        log.info("delete user");
        String key = CacheConstant.USER + id;

        this.user = null;
        cache.invalidate(key);
    }

    @Override
    public void reload() {
        user = new User();
        user.setId(1);
        user.setUsername("small orange");
        user.setPassword("123456");

        // 所有缓存都失效
        cache.invalidateAll();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        this.reload();
    }
}
