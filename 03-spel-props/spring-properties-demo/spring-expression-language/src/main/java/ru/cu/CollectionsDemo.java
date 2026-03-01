package ru.cu;

import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.List;
import java.util.Map;

import static ru.cu.helper.DemoUtils.showValueWithRealAndExpectedTypes;

// 05
public class CollectionsDemo {

    public static void main(String[] args) {
        // Работа с коллекциями
        ExpressionParser parser = new SpelExpressionParser();

        var val = parser.parseExpression("{1, 2, 3}").getValue(); // List
        showValueWithRealAndExpectedTypes(val, List.class);

        val = parser.parseExpression("{'a':1, 'b':2}").getValue(); // Map
        showValueWithRealAndExpectedTypes(val, Map.class);

        var context = new StandardEvaluationContext();
        context.setVariable("list", List.of(1, 2, 3));
        context.setVariable("map", Map.of("a", 1, "b", 2));

        val = parser.parseExpression("#list[0]").getValue(context); // 1
        showValueWithRealAndExpectedTypes(val, Integer.class);

        val = parser.parseExpression("#map['a']").getValue(context); // 1
        showValueWithRealAndExpectedTypes(val, Integer.class);

        val = parser.parseExpression("#list.?[#this > 1]").getValue(context); // [2, 3] (фильтрация)
        showValueWithRealAndExpectedTypes(val, List.class);

        val = parser.parseExpression("#list.^[#this > 1]").getValue(context); // 2 (первый подходящий)
        showValueWithRealAndExpectedTypes(val, Integer.class);

        val = parser.parseExpression("#list.$[#this > 1]").getValue(context); // 3 (последний подходящий)
        showValueWithRealAndExpectedTypes(val, Integer.class);
    }

}
