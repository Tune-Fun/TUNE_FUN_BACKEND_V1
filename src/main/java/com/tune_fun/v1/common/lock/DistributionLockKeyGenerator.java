package com.tune_fun.v1.common.lock;

import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

public class DistributionLockKeyGenerator {
    public static Object generate(String methodName, String[] parameterNames, Object[] args,
                                  String key) {
        ExpressionParser parser = new SpelExpressionParser();
        StandardEvaluationContext context = new StandardEvaluationContext();
        for (int i = 0; i < parameterNames.length; i++) context.setVariable(parameterNames[i], args[i]);
        return methodName + "-" + parser.parseExpression(key).getValue(context, Object.class);
    }
}
