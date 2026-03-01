package ru.cu;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import ru.cu.config.ConfigWithMethodInjection;
import ru.cu.config.ServicesConfigWithAspectJProxy;
import ru.cu.config.ServicesConfigWithCGLibProxy;
import ru.cu.config.ServicesConfigWithSpringStyleProxy;
import ru.cu.services.ActionsService;
import ru.cu.services.InformationService;

@ComponentScan(basePackages = "ru.cu.services")
@EnableAspectJAutoProxy
//@EnableAspectJAutoProxy(proxyTargetClass = true)
public class AopDemoApplication {

	public static void main(String[] args) {
		var ctx = new AnnotationConfigApplicationContext();
		showAopDemo(ctx, ServicesConfigWithSpringStyleProxy.class);
        //showAopDemo(ctx, ServicesConfigWithAspectJProxy.class);
        //showConfigMethodInjectDemo(ctx);
        //showAopDemo(ctx, ServicesConfigWithCGLibProxy.class);
	}

	private static void showAopDemo(AnnotationConfigApplicationContext ctx, Class<?> configClass){
        ctx.register(AopDemoApplication.class, configClass);
        ctx.refresh();

		if (!ctx.containsBean("actionsService1")) {
			return;
		}

		var actionsService1 = ctx.getBean("actionsService1", ActionsService.class);
		var actionsService2 = ctx.getBean("actionsService2", ActionsService.class);

		System.out.println("actionsService1, class: " + actionsService1.getClass());
        actionsService1.doFastAction();
		System.out.println();
        actionsService1.doSlowAction();
		System.out.println();
		System.out.println();

		System.out.println("----------------------------------------------------------");

        System.out.println("actionsService2, class: " + actionsService2.getClass());
        actionsService2.doFastAction();
		System.out.println();
        actionsService2.doSlowAction();
		System.out.println();
		System.out.println();
	}


	private static void showConfigMethodInjectDemo(AnnotationConfigApplicationContext ctx) {
        ctx.register(ConfigWithMethodInjection.class);
        ctx.refresh();

		var informationService1 = ctx.getBean("informationService1", InformationService.class);
		var informationService2 = ctx.getBean("informationService2", InformationService.class);
		var informationService3 = ctx.getBean("informationService3", InformationService.class);

        informationService1.showServiceInfo();
        informationService2.showServiceInfo();
        informationService3.showServiceInfo();
	}
}
