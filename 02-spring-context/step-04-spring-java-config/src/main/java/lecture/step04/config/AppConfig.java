package lecture.step04.config;

import lecture.step04.controller.CategoryController;
import lecture.step04.repo.CategoryRepository;
import lecture.step04.repo.DbCategoryRepository;
import lecture.step04.service.*;
import lecture.step04.service.io.ConsoleIoService;
import lecture.step04.service.io.IoService;
import lecture.step04.service.validator.CategoryValidator;
import lecture.step04.service.validator.LenientCategoryValidator;
import lecture.step04.service.validator.StrictCategoryValidator;
import lecture.step04.storage.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

@Configuration
//@Configuration(proxyBeanMethods = false)
//@Component
public class AppConfig {

    // Strategy #1: Storage
    @Bean
    public Storage storage() {
        return new InMemoryStorage();
    }

    @Bean
    public CategoryRepository categoryRepository() {
        return new DbCategoryRepository(storage());
    }

    @Bean
    public IoService ioService() {
        return new ConsoleIoService();
    }


    // ============================================================
    // Validator (несколько реализаций одного интерфейса)
    // ВНИМАНИЕ: если в контексте 2 бина одного типа, внедрение "по типу"
    // станет неоднозначным. Ниже — варианты как это решать.
    // ============================================================

    // --- Вариант А: два валидатора одновременно (выбор через Qualifier / явную сборку)
//    @Bean
//    public CategoryValidator lenientCategoryValidator() {
//        return new LenientCategoryValidator();
//    }
//
//    @Bean
//    public CategoryValidator strictCategoryValidator() {
//        return new StrictCategoryValidator();
//    }

    // --- Вариант B: один "по умолчанию" через @Primary
     @Bean
     @Primary
     public CategoryValidator strictCategoryValidator() {
         return new StrictCategoryValidator();
     }

     @Bean
     public CategoryValidator lenientCategoryValidator() {
         return new LenientCategoryValidator();
     }

    // --- Вариант C: профили (в активном профиле будет создан только один бин)
//     @Bean
//     @Profile("dev")
//     public CategoryValidator lenientCategoryValidator() {
//         return new LenientCategoryValidator();
//     }
//
//     @Bean
//     @Profile("prod")
//     public CategoryValidator strictCategoryValidator() {
//         return new StrictCategoryValidator();
//     }

    @Bean
//    @Bean(initMethod = "init", destroyMethod = "shutdown")
//    @Lazy
    public CategoryService categoryService(CategoryRepository repo, CategoryValidator strictCategoryValidator) {
        return new CategoryService(repo, strictCategoryValidator);
    }

    @Bean
//    @Profile("prod")
    public CategoryController categoryController(CategoryService service) {
        return new CategoryController(service, ioService());
    }

    @Bean
    public CategoryService categoryService(List<CategoryValidator> validators) {
        return new CategoryService(validators);
    }

}
