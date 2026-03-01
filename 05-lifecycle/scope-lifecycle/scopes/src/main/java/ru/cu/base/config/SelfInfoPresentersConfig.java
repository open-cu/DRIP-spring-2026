package ru.cu.base.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import ru.cu.base.service.SelfInfoPresenter;
import ru.cu.base.service.SelfInfoPresenterImpl;

@Configuration
public class SelfInfoPresentersConfig {

    @Bean
    public SelfInfoPresenter singletonScopeBean() {
        return new SelfInfoPresenterImpl();
    }

    @Scope(scopeName = "prototype")
    @Bean
    public SelfInfoPresenter prototypeScopeBean() {
        return new SelfInfoPresenterImpl();
    }

    @Scope(scopeName = "prototype", proxyMode = ScopedProxyMode.INTERFACES)
    @Bean
    public SelfInfoPresenter proxiedPrototypeScopeBean() {
        return new SelfInfoPresenterImpl();
    }

    @Scope(scopeName = "session", proxyMode = ScopedProxyMode.INTERFACES)
    @Bean
    public SelfInfoPresenter sessionScopeBean() {
        return new SelfInfoPresenterImpl();
    }

    @Scope(scopeName = "request", proxyMode = ScopedProxyMode.INTERFACES)
    @Bean
    public SelfInfoPresenter requestScopeBean() {
        return new SelfInfoPresenterImpl();
    }
}
