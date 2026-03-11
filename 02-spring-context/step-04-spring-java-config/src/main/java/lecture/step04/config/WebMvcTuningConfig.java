package lecture.step04.config;


import org.springframework.context.annotation.Configuration;

//@Configuration
//этот класс может быть нужен только ради переопределения “хуков” WebMvcConfigurer —
// чтобы Spring MVC подхватил настройки (CORS/интерцепторы/ресурсы/роуты), даже если тут нет ни одного @Bean.
//public class WebMvcTuningConfig implements WebMvcConfigurer {
//
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        registry.addMapping("/api/**")
//                .allowedMethods("GET", "POST", "PUT", "DELETE");
//    }
//
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(new RequestLogInterceptor());
//    }
//
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("/static/**")
//                .addResourceLocations("classpath:/static/");
//    }
//
//    @Override
//    public void addViewControllers(ViewControllerRegistry registry) {
//        registry.addViewController("/health").setViewName("forward:/actuator/health");
//    }
//}