package com.common.distributedlock;

/**
 * <一句话描述>
 *
 * @author huangzy
 * @version 2.0
 * @since 2.0
 * <p>
 * created on 2018/8/21 12:33
 */
public class LockException extends RuntimeException{
    public LockException(String message) {
        super(message);
    }
}
