package com.common.distributedlock.helper;

import java.util.stream.Stream;

/**
 * 默认key生成方式
 *
 * @author huangzy
 * @version 2.0
 * @since 2.0
 * <p>
 * created on 2018/8/21 13:53
 */
public class DefaultGenerateKey implements GenerateKey {
    private String className;
    private String methodName;
    private Object[] args;

    public DefaultGenerateKey(String className, String methodName, Object[] args) {
        this.className = className;
        this.methodName = methodName;
        this.args = args;
    }

    @Override
    public String generate(String annotKeyVariable) {
        String newKey = className+methodName;;
        if (args != null && args.length > 0) {
            StringBuilder argSB = new StringBuilder();
            Stream.of(args).forEach(arg->{
                if (arg != null) {
                    argSB.append(arg.toString());
                }
            });
            newKey = newKey + argSB.toString();
        }
        return newKey;
    }
}
