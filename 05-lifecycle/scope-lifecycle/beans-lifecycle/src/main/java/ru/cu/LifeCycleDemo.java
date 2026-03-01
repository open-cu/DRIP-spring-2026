package ru.cu;

import org.springframework.context.annotation.PropertySource;
import ru.cu.service.ImportantService;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@PropertySource("classpath:application.properties")
@ComponentScan
public class LifeCycleDemo {

	public static void main(String[] args) {
		var ctx = new AnnotationConfigApplicationContext(LifeCycleDemo.class);
		var stepsPrintingRegimeEnabled = ctx.getEnvironment()
                .getProperty("steps.printing.regime.enabled", Boolean.class, true);
		if (stepsPrintingRegimeEnabled) {
            ctx.close();
            return;
        }
        var worker = ctx.getBean(ImportantService.class);
        worker.doWork();
	}

}
