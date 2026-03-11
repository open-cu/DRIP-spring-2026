package lecture.step05.config;

import lecture.step05.service.CategoryService;
import lecture.step05.service.validator.CategoryValidator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.List;

//@Configuration
@ComponentScan
//@Import({RepoConfig.class})
public class AppConfig {
//    @Bean
//    public CategoryService categoryService(List<CategoryValidator> validators) {
//
//        return new CategoryService(validators);
//    }


}
