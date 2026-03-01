package ru.cu;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import ru.cu.config.AppConfig;
import ru.cu.domain.Person;

import java.time.LocalDate;

import static ru.cu.helper.DemoUtils.showValueWithRealAndExpectedTypes;

// 07
public class BeansDemo {

    public static void main(String[] args) {
        var applicationContext = new AnnotationConfigApplicationContext(AppConfig.class);
        var person = new Person(1L, "Ivan", "Ivanov", LocalDate.of(1981, 8, 20));

        ExpressionParser parser = new SpelExpressionParser();
        var evaluationContext = new StandardEvaluationContext();
        evaluationContext.setBeanResolver(new BeanFactoryResolver(applicationContext));
        evaluationContext.setRootObject(person);

        var val = parser.parseExpression("@objectMapper.writeValueAsString(#root)")
                .getValue(evaluationContext);
        showValueWithRealAndExpectedTypes(val, String.class);
    }

}
