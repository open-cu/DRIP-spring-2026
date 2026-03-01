package ru.cu.lecture;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import ru.cu.lecture.props.SimpleProps;
import ru.cu.lecture.props.SpElProps;


@ComponentScan
@Configuration
public class SpringPropertiesDemo {

	public static void main(String[] args) {
		// Environment var: not.really.existing.prop=Не видишь суслика, а он есть (с)
		// VM Option: -Dnot.really.existing.prop="Не видишь суслика, а он есть 2 (с)"
		// System.setProperty("not.really.existing.prop", "Не видишь суслика, а он есть 3 (с)");

		var ctx = new AnnotationConfigApplicationContext(SpringPropertiesDemo.class);

		printPropertiesBeanValues(ctx, SimpleProps.class);
		//printPropertiesBeanValues(ctx, SpElProps.class);
		//printPropertiesBeanValues(ctx, CustomProps.class);
		//printPropertiesBeanValues(ctx, YamlProps.class);
	}

	private static void printPropertiesBeanValues(ApplicationContext ctx, Class<?> propertiesBeanClass) {
		var propertiesBean = ctx.getBean(propertiesBeanClass);
		System.out.println(propertiesBean);
		System.out.println("------------------------------------------------------");
	}
}
