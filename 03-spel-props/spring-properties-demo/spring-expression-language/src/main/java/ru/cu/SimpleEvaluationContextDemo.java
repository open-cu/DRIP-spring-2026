package ru.cu;

import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.SimpleEvaluationContext;
import ru.cu.domain.Person;

import java.time.LocalDate;

import static ru.cu.helper.DemoUtils.showValueWithRealAndExpectedTypes;

// 07
public class SimpleEvaluationContextDemo {

    public static void main(String[] args) throws NoSuchMethodException {
        var person = new Person(1L, "Ivan", "Ivanov",
                LocalDate.of(1981, 8, 20));

        ExpressionParser parser = new SpelExpressionParser();
        var context = SimpleEvaluationContext
                .forReadOnlyDataBinding()
/*
                .forPropertyAccessors(new ReflectivePropertyAccessor() {
                    @Override
                    public boolean canRead(@Nonnull EvaluationContext context,
                                           Object target,
                                           @Nonnull String name) throws AccessException {
                        return !"lastName".equals(name);
                    }
                })
*/
                .withRootObject(person)
                //.withInstanceMethods()
                .build();

/*
        // Не сработает
        Person person2 = parser.parseExpression(
                "new ru.cu.domain.Person(2L, 'Petr', 'Petrov', birthDate)"
        ).getValue(context, Person.class);
*/

        var val = parser.parseExpression("'abcd'.length()").getValue(context);
        showValueWithRealAndExpectedTypes(val, Integer.class);

        val = parser.parseExpression("name").getValue(context);
        showValueWithRealAndExpectedTypes(val, String.class);

        val = parser.parseExpression("lastName").getValue(context);
        showValueWithRealAndExpectedTypes(val, String.class);

/*
        // Не сработает
        val = parser.parseExpression("T(java.time.LocalDate).of(1981, 8, 20))").getValue();
        showValueWithRealAndExpectedTypes(val, LocalDate.class);
*/
    }

}
