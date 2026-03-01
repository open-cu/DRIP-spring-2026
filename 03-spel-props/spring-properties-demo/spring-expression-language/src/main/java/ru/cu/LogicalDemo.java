package ru.cu;

import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import static ru.cu.helper.DemoUtils.showValueWithRealAndExpectedTypes;

// 02
public class LogicalDemo {
    public static void main(String[] args) {
        //Логические операции
        ExpressionParser parser = new SpelExpressionParser();

        var val = parser.parseExpression("true and false").getValue(); // false
        showValueWithRealAndExpectedTypes(val, Boolean.class);

        val = parser.parseExpression("true or false").getValue(); // true
        showValueWithRealAndExpectedTypes(val, Boolean.class);

        val = parser.parseExpression("not false").getValue(); // true
        showValueWithRealAndExpectedTypes(val, Boolean.class);

        val = parser.parseExpression("1 == 1").getValue(); // true
        showValueWithRealAndExpectedTypes(val, Boolean.class);

        val = parser.parseExpression("1 != 2").getValue(); // true
        showValueWithRealAndExpectedTypes(val, Boolean.class);

        val = parser.parseExpression("1 < 2").getValue(); // true
        showValueWithRealAndExpectedTypes(val, Boolean.class);

        val = parser.parseExpression("1 <= 1").getValue(); // true
        showValueWithRealAndExpectedTypes(val, Boolean.class);
    }

}
