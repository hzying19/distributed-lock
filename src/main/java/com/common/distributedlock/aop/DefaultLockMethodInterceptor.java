package com.common.distributedlock.aop;

import com.common.distributedlock.LockException;
import com.common.distributedlock.annot.Distributedlock;
import com.common.distributedlock.helper.DefaultGenerateKey;
import com.common.distributedlock.helper.GenerateKey;
import com.common.distributedlock.helper.SpringELGenerateKey;
import com.common.distributedlock.lock.IDistributedLock;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.reflect.Method;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.UUID;

/**
 * <一句话描述>
 *
 * @author huangzy
 * @version 2.0
 * @since 2.0
 * <p>
 * created on 2018/8/21 12:14
 */
public class DefaultLockMethodInterceptor implements MethodInterceptor {
    private IDistributedLock distributedLock;

    public DefaultLockMethodInterceptor(IDistributedLock distributedLock) {
        this.distributedLock = distributedLock;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Method method = invocation.getMethod();
        Object[] args = invocation.getArguments();

        Distributedlock distributedlockAnnot = method.getAnnotation(Distributedlock.class);

        String annotValue = distributedlockAnnot.value();
        GenerateKey generateKey = generateKeyInstance(invocation.getThis().getClass().getName(),method,args,annotValue);

        //获取分布式锁
        String requestId = UUID.randomUUID().toString().replaceAll("-","");
        String lockKey = generateKey.generate(annotValue);
        int expireTime = distributedlockAnnot.expireTime();
        int waitTime = distributedlockAnnot.waitTime();
        String md5LockKey = getMD5(lockKey);

        boolean getLock;
        if (waitTime == 0) {
            getLock = distributedLock.tryGetLock(md5LockKey, requestId, expireTime);
        }else {
            getLock = distributedLock.tryGetLock(md5LockKey, requestId, expireTime, waitTime);
        }

        if (!getLock) {
            throw new LockException("获取不到分布式锁");
        }

        Object result = null;
        try {
            result = invocation.proceed();
        }finally {
            //释放分布式锁
            distributedLock.unlock(md5LockKey, requestId);
        }

        return result;
    }

    private GenerateKey generateKeyInstance(String className, Method method, Object[] args,String annotKeyVariable) {
        //未指定key
        if (annotKeyVariable == null||"".equals(annotKeyVariable)) {
            return new DefaultGenerateKey(className,method.getName(),args);
        }
        //指定key
        return new SpringELGenerateKey(method,args);
    }

    private String getMD5(String str) {
        try {
            // 生成一个MD5加密计算摘要
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 计算md5函数
            md.update(str.getBytes());
            // digest()最后确定返回md5 hash值，返回值为8为字符串。因为md5 hash值是16位的hex值，实际上就是8位的字符
            // BigInteger函数则将8位的字符串转换成16位hex值，用字符串来表示；得到字符串形式的hash值
            return new BigInteger(1, md.digest()).toString(16);
        } catch (Exception e) {
            throw new LockException("MD5加密出现错误");
        }
    }
}
