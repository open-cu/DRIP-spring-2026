package lecture.step05;

import lecture.step05.config.AppConfig;
import lecture.step05.controller.CategoryController;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

public class App {
    public static void main(String[] args) {
        var ctx = new AnnotationConfigApplicationContext();
        ctx.getBean(CategoryController.class).run();
        ctx.close();
    }
}
