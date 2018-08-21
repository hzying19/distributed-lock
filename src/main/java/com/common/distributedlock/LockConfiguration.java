package com.common.distributedlock;

import com.common.distributedlock.aop.DefaultLockMethodInterceptor;
import com.common.distributedlock.annot.Distributedlock;
import com.common.distributedlock.lock.IDistributedLock;
import com.common.distributedlock.lock.RedisDistributedLockImpl;
import org.aopalliance.aop.Advice;
import org.springframework.aop.Advisor;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.annotation.AnnotationMatchingPointcut;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * <一句话描述>
 *
 * @author huangzy
 * @version 2.0
 * @since 2.0
 * <p>
 * created on 2018/8/21 11:29
 */
@Configuration
//@ConditionalOnBean(RedisTemplate.class)
public class LockConfiguration {
    @Bean
    @ConditionalOnMissingBean(IDistributedLock.class)
    public IDistributedLock redisDistributedLock() {
        IDistributedLock iDistributedLock = getIDistributedLockFromSPI();
        if (iDistributedLock != null) {
            return iDistributedLock;
        }
        return new RedisDistributedLockImpl();
    }

    @Bean("_$lockPointcut")
    public Pointcut pointcut() {
        return AnnotationMatchingPointcut.forMethodAnnotation(Distributedlock.class);
    }

    @Bean("_$lockAdvice")
    public Advice advice(IDistributedLock distributedLock) {
        return new DefaultLockMethodInterceptor(distributedLock);
    }

    @Bean("_$lockAdvisor")
    public Advisor advisor(@Qualifier("_$lockPointcut") Pointcut pointcut, @Qualifier("_$lockAdvice") Advice advice) {
        return new DefaultPointcutAdvisor(pointcut,advice);
    }

    private IDistributedLock getIDistributedLockFromSPI() {
        ServiceLoader<IDistributedLock> serviceLoader = ServiceLoader.load(IDistributedLock.class);
        Iterator<IDistributedLock> iterator = serviceLoader.iterator();

        IDistributedLock iDistributedLock = null;
        if (iterator != null&& iterator.hasNext()) {
            if (iDistributedLock != null) {
                throw new LockException("只有存在一个IDistributedLock SPI扩展实现");
            }
            iDistributedLock = iterator.next();
        }
        return iDistributedLock;
    }
}
