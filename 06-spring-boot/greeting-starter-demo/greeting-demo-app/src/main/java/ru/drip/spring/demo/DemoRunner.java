package ru.drip.spring.demo;

import java.util.Arrays;
import java.util.Locale;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import ru.drip.spring.greeting.autoconfigure.DevBanner;
import ru.drip.spring.greeting.autoconfigure.GreetingService;

@Component
public class DemoRunner implements ApplicationRunner {

    private final GreetingService greetingService;
    private final MessageSource messageSource;
    private final DemoProperties properties;
    private final NotificationService notificationService;
    private final ApplicationContext applicationContext;
    private final ObjectProvider<DevBanner> devBannerProvider;
    private final ObjectProvider<ExperimentalFeature> experimentalFeatureProvider;

    public DemoRunner(
            GreetingService greetingService,
            MessageSource messageSource,
            DemoProperties properties,
            NotificationService notificationService,
            ApplicationContext applicationContext,
            ObjectProvider<DevBanner> devBannerProvider,
            ObjectProvider<ExperimentalFeature> experimentalFeatureProvider
    ) {
        this.greetingService = greetingService;
        this.messageSource = messageSource;
        this.properties = properties;
        this.notificationService = notificationService;
        this.applicationContext = applicationContext;
        this.devBannerProvider = devBannerProvider;
        this.experimentalFeatureProvider = experimentalFeatureProvider;
    }

    @Override
    public void run(ApplicationArguments args) {
        Locale locale = Locale.forLanguageTag(properties.getLocale());

        System.out.println("profiles.active=" + Arrays.toString(applicationContext.getEnvironment().getActiveProfiles()));
        System.out.println("beans.greeting=" + Arrays.toString(applicationContext.getBeanNamesForType(GreetingService.class)));
        System.out.println("beans.notification=" + Arrays.toString(applicationContext.getBeanNamesForType(NotificationService.class)));
        System.out.println("beans.devBanner=" + Arrays.toString(applicationContext.getBeanNamesForType(DevBanner.class)));
        System.out.println("beans.experimental=" + Arrays.toString(applicationContext.getBeanNamesForType(ExperimentalFeature.class)));

        DevBanner devBanner = devBannerProvider.getIfAvailable();
        if (devBanner != null) {
            System.out.println(devBanner.banner());
        }

        System.out.println("demo.locale=" + properties.getLocale() + " resolvedLocale=" + locale);

        String welcome = messageSource.getMessage(
                "app.welcome",
                new Object[]{greetingService.greet()},
                locale
        );
        System.out.println(welcome);

        System.out.println("demo.tags=" + properties.getTags());
        System.out.println("demo.meta=" + properties.getMeta());

        ExperimentalFeature experimentalFeature = experimentalFeatureProvider.getIfAvailable();
        if (experimentalFeature != null) {
            System.out.println(experimentalFeature.message());
        }

        notificationService.notify(welcome);
    }
}
