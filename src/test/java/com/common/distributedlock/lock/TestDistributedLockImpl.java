package com.common.distributedlock.lock;

/**
 * test分布式锁
 *
 * @author huangzy
 * @version 2.0
 * @since 2.0
 * <p>
 * created on 2018/7/3 15:49
 */
public class TestDistributedLockImpl implements IDistributedLock {

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
        return true;
    }


    /**
     * 释放分布式锁
     * @param lockKey 锁
     * @param requestId 请求标识
     * @return 是否释放成功
     */
    @Override
    public boolean unlock(String lockKey, String requestId) {
        return true;
    }

}
