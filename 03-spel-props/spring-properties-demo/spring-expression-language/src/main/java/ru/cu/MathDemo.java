package ru.cu;

import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import static ru.cu.helper.DemoUtils.showValueWithRealAndExpectedTypes;

// 04
public class MathDemo {

    public static void main(String[] args) {
        // Математические операции
        ExpressionParser parser = new SpelExpressionParser();

        var val = parser.parseExpression("2 + 2 * 3").getValue(); // 8
        showValueWithRealAndExpectedTypes(val, Integer.class);

        val = parser.parseExpression("10 / 2").getValue(); // 5
        showValueWithRealAndExpectedTypes(val, Double.class);

        val = parser.parseExpression("10 % 3").getValue(); // 1
        showValueWithRealAndExpectedTypes(val, Integer.class);

        val = parser.parseExpression("2 ^ 3").getValue(); // 8 (степень)
        showValueWithRealAndExpectedTypes(val, Integer.class);
    }
}
