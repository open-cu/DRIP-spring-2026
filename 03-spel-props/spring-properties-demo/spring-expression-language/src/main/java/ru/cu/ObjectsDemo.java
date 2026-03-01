package ru.cu;

import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import ru.cu.converter.LocalDateConversionUtils;
import ru.cu.domain.Person;

import java.time.LocalDate;

import static ru.cu.helper.DemoUtils.showValueWithRealAndExpectedTypes;

// 06
public class ObjectsDemo {

    public static void main(String[] args) throws NoSuchMethodException {
        // Работа с объектами
        ExpressionParser parser = new SpelExpressionParser();
        var context = new StandardEvaluationContext();

        var val = parser.parseExpression("'abc'.length()").getValue(); // 3
        showValueWithRealAndExpectedTypes(val, Integer.class);

        val = parser.parseExpression("'abc'.toUpperCase()").getValue(); // "ABC"
        showValueWithRealAndExpectedTypes(val, String.class);

        Person person = parser.parseExpression(
                "new ru.cu.domain.Person(1L, 'Ivan', 'Ivanov', T(java.time.LocalDate).of(1981, 8, 20))"
        ).getValue(Person.class);

        val = parser.parseExpression("name").getValue(person); // "Ivan"
        showValueWithRealAndExpectedTypes(val, String.class);

        val = parser.parseExpression("name + ' ' + lastName").getValue(person); // "Ivan Ivanov"
        showValueWithRealAndExpectedTypes(val, String.class);

        val = parser.parseExpression("birthDate").getValue(person); // "1981-08-20"
        showValueWithRealAndExpectedTypes(val, LocalDate.class);

        context.setVariable("person", person);
        context.registerFunction("formatDate",
                LocalDateConversionUtils.class.getDeclaredMethod("formatDate", LocalDate.class, String.class));

        val = parser.parseExpression("'{\"id\": \"' + #person.id + " +
                "'\", \"name\": \"' + #person.name + " +
                "'\", \"lastName\": \"' + #person.lastName + " +
                "'\", \"birthDate\": \"' + #formatDate(#person.birthDate, 'dd.MM.yyyy') + '\"}'"
        ).getValue(context);
        showValueWithRealAndExpectedTypes(val, String.class);
    }

}
