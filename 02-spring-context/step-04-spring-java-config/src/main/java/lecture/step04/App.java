package lecture.step04;

import lecture.step04.config.AppConfig;
import lecture.step04.controller.CategoryController;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class App {
    public static void main(String[] args) {
        var ctx = new AnnotationConfigApplicationContext(AppConfig.class);
        ctx.getBean(CategoryController.class).run();
        ctx.close();
    }
}
