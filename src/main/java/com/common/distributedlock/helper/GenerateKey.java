package com.common.distributedlock.helper;

/**
 * key生成方式
 *
 * @author huangzy
 * @version 2.0
 * @since 2.0
 * <p>
 * created on 2018/8/21 13:43
 */
public interface GenerateKey {
    String generate(String annotKeyVariable);
}
