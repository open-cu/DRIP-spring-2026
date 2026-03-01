package ru.cu;

import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import static ru.cu.helper.DemoUtils.showValueWithRealAndExpectedTypes;

// 01
public class LiteralsDemo {


    public static void main(String[] args) {
        // Литералы
        ExpressionParser parser = new SpelExpressionParser();

        var val = parser.parseExpression("123").getValue(); // 123 (int)
        showValueWithRealAndExpectedTypes(val, Integer.class);

        val = parser.parseExpression("3.14159").getValue(); // 3.14159 (double)
        showValueWithRealAndExpectedTypes(val, Double.class);

        val = parser.parseExpression("'Hello'").getValue(); // "Hello" (String)
        showValueWithRealAndExpectedTypes(val, String.class);

        val = parser.parseExpression("true").getValue(); // true (boolean)
        showValueWithRealAndExpectedTypes(val, Boolean.class);

        val = parser.parseExpression("null").getValue(); // null
        showValueWithRealAndExpectedTypes(val, Object.class);
    }

}
