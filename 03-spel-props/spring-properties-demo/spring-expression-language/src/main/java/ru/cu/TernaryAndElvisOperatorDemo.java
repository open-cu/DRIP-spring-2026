package ru.cu;

import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import static ru.cu.helper.DemoUtils.showValueWithRealAndExpectedTypes;

// 03
public class TernaryAndElvisOperatorDemo {
    public static void main(String[] args)  {
        // Тернарный оператор и Elvis-оператор
        ExpressionParser parser = new SpelExpressionParser();

        var val = parser.parseExpression("true ? 'yes' : 'no'").getValue(); // "yes"
        showValueWithRealAndExpectedTypes(val, String.class);

        val = parser.parseExpression("#root ?: 'default'").getValue("Vasya"); // Elvis-оператор
        showValueWithRealAndExpectedTypes(val, String.class);

        val = parser.parseExpression("#root ?: 'default'").getValue(); // Elvis-оператор
        showValueWithRealAndExpectedTypes(val, String.class);

        val = parser.parseExpression("#root?.length()").getValue("Vasya"); // Elvis-оператор
        showValueWithRealAndExpectedTypes(val, Integer.class);

        val = parser.parseExpression("#root?.length()").getValue(); // Elvis-оператор
        showValueWithRealAndExpectedTypes(val, Integer.class);

        val = parser.parseExpression("#root?.length() ?: 111").getValue(); // Elvis-оператор
        showValueWithRealAndExpectedTypes(val, Integer.class);
    }

}
