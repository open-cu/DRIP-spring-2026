package lecture.step03;

import lecture.step03.controller.CategoryController;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {
    public static void main(String[] args) {
        var ctx = new ClassPathXmlApplicationContext("application-context.xml");
        CategoryController controller = ctx.getBean(CategoryController.class);
        controller.run();
    }
}
