package com.common.distributedlock.lock;

import com.common.distributedlock.LockException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisCommands;

import java.util.Collections;

/**
 * Redis分布式锁
 *
 * @author huangzy
 * @version 2.0
 * @since 2.0
 * <p>
 * created on 2018/7/3 15:49
 */
public class RedisDistributedLockImpl implements IDistributedLock {
    private static final String LOCK_SUCCESS = "OK";
    private static final String SET_IF_NOT_EXIST = "NX";
    private static final String SET_WITH_EXPIRE_TIME = "EX";

    private static final Long RELEASE_SUCCESS = 1L;

    private RedisTemplate redisTemplate;

    /**
     * 尝试获取分布式锁
     * @param lockKey
     * @param requestId
     * @param expireTime 过期时间 单位是秒
     * @param waitTime 等待时间 单位是秒
     * @return
     */
    @Override
    public boolean tryGetLock(String lockKey, String requestId, int expireTime,int waitTime) {
        if (waitTime < 0) {
            throw new LockException("waitTime不能小于0");
        }

        boolean lock = false;
        long oldCurrentTimeMillis = System.currentTimeMillis() + waitTime*1000;
        while (!lock
                && System.currentTimeMillis()<oldCurrentTimeMillis) {

            lock = tryGetLock(lockKey,requestId,expireTime);

            //获取不到锁，休眠1秒。
            if (!lock) {
                try {
                    Thread.sleep(1*1000);
                } catch (InterruptedException e) {}
            }

        }
        return lock;
    }

    /**
     * 尝试获取分布式锁
     * @param lockKey 锁
     * @param requestId 请求标识
     * @param expireTime 超期时间 单位是秒
     * @return 是否获取成功
     */
    @Override
    public boolean tryGetLock(String lockKey, String requestId, int expireTime) {
        if (expireTime < 0) {
            throw new LockException("expireTime不能小于0");
        }

        String result = (String)redisTemplate.execute((RedisCallback)connection -> {
            JedisCommands commands = (JedisCommands) connection.getNativeConnection();
            return commands.set(lockKey, requestId, SET_IF_NOT_EXIST, SET_WITH_EXPIRE_TIME, expireTime);
        });

        return LOCK_SUCCESS.equals(result);
    }


    /**
     * 释放分布式锁
     * @param lockKey 锁
     * @param requestId 请求标识
     * @return 是否释放成功
     */
    @Override
    public boolean unlock(String lockKey, String requestId) {

        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";

        Long result = (Long) redisTemplate.execute((RedisCallback)connection->{
            Object nativeConnection = connection.getNativeConnection();
               // 集群模式
               if (nativeConnection instanceof JedisCluster) {
                   return (Long) ((JedisCluster) nativeConnection).eval(script, Collections.singletonList(lockKey), Collections.singletonList(requestId));
               }

               // 单机模式
               else if (nativeConnection instanceof Jedis) {
                   return (Long) ((Jedis) nativeConnection).eval(script, Collections.singletonList(lockKey), Collections.singletonList(requestId));
               }
               return 0L;
        });

        return RELEASE_SUCCESS.equals(result);
    }

    @Autowired
    public void setRedisTemplate(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
}
