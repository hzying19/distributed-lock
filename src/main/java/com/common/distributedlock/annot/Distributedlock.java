package com.common.distributedlock.annot;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 分布式锁
 *
 * @author huangzy
 * @version 2.0
 * @since 2.0
 * <p>
 * created on 2018/8/21 10:48
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Distributedlock {

    /**
     * key表达式，使用spring el表达式。
     * @return
     */
    String value() default "";

    /**
     * 满足condition条件，则分布式锁生效。(预留,未实现)
     * @return
     */
    String condition() default "";

    /**
     * 锁超时时间(锁定expireTime秒，超时则自动释放锁)
     * @return
     */
    int expireTime() default 360;

    /**
     * 锁等待时间(waitTime秒内，不停的尝试获取锁)
     * @return
     */
    int waitTime() default 0;
}
