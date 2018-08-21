package com.common.distributedlock.lock;

/**
 * <一句话描述>
 *
 * @author huangzy
 * @version 2.0
 * @since 2.0
 * <p>
 * created on 2018/8/21 11:09
 */
public interface IDistributedLock {
    /**
     * 尝试获取分布式锁
     *
     * @param lockKey
     * @param requestId
     * @param expireTime    过期时间 单位是秒
     * @param waitTime      等待时间 单位是秒
     * @return
     */
    boolean tryGetLock(String lockKey, String requestId, int expireTime, int waitTime);

    /**
     * 尝试获取分布式锁
     *
     * @param lockKey       锁
     * @param requestId     请求标识
     * @param expireTime    超期时间 单位是秒
     * @return 是否获取成功
     */
    boolean tryGetLock(String lockKey, String requestId, int expireTime);


    /**
     * 释放分布式锁
     *
     * @param lockKey       锁
     * @param requestId     请求标识
     * @return 是否释放成功
     */
    boolean unlock(String lockKey, String requestId);
}
