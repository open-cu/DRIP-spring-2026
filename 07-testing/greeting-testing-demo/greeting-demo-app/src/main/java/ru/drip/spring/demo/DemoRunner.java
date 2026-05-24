package ru.drip.spring.demo;

import java.util.Arrays;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import ru.drip.spring.greeting.autoconfigure.DevBanner;
import ru.drip.spring.greeting.autoconfigure.GreetingService;

@Component
public class DemoRunner implements ApplicationRunner {

    private final DemoProperties properties;
    private final WelcomeService welcomeService;
    private final ApplicationContext applicationContext;
    private final ObjectProvider<DevBanner> devBannerProvider;
    private final ObjectProvider<ExperimentalFeature> experimentalFeatureProvider;

    public DemoRunner(
            DemoProperties properties,
            WelcomeService welcomeService,
            ApplicationContext applicationContext,
            ObjectProvider<DevBanner> devBannerProvider,
            ObjectProvider<ExperimentalFeature> experimentalFeatureProvider
    ) {
        this.properties = properties;
        this.welcomeService = welcomeService;
        this.applicationContext = applicationContext;
        this.devBannerProvider = devBannerProvider;
        this.experimentalFeatureProvider = experimentalFeatureProvider;
    }

    @Override
    public void run(ApplicationArguments args) {
        var locale = welcomeService.resolveLocale();

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

        String welcome = welcomeService.buildWelcomeMessage();
        System.out.println(welcome);

        System.out.println("demo.tags=" + properties.getTags());
        System.out.println("demo.meta=" + properties.getMeta());

        ExperimentalFeature experimentalFeature = experimentalFeatureProvider.getIfAvailable();
        if (experimentalFeature != null) {
            System.out.println(experimentalFeature.message());
        }

        welcomeService.sendWelcomeNotification(welcome);
    }
}
