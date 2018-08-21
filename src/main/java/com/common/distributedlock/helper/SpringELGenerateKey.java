package com.common.distributedlock.helper;

import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;

/**
 * 支持sprign el 表达式的key生成方式
 *
 * @author huangzy
 * @version 2.0
 * @since 2.0
 * <p>
 * created on 2018/8/21 13:55
 */
public class SpringELGenerateKey implements GenerateKey{
    private static ExpressionParser parser = new SpelExpressionParser();
    private static LocalVariableTableParameterNameDiscoverer discoverer = new LocalVariableTableParameterNameDiscoverer();

    private Method method;
    private Object[] args;

    public SpringELGenerateKey(Method method, Object[] args) {
        this.method = method;
        this.args = args;
    }

    @Override
    public String generate(String annotKeyVariable) {
        String[] params = discoverer.getParameterNames(method);
        EvaluationContext context = new StandardEvaluationContext();
        for (int len = 0; len < params.length; len++) {
            context.setVariable(params[len], args[len]);
        }
        Expression expression = parser.parseExpression(annotKeyVariable);
        return expression.getValue(context, String.class);
    }
}
