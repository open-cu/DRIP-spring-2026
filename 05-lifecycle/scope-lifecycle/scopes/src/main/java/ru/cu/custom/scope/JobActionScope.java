package ru.cu.custom.scope;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static ru.cu.custom.scope.JobActionScopeImpl.SCOPE_NAME;

@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Scope(scopeName = SCOPE_NAME, proxyMode = ScopedProxyMode.TARGET_CLASS)
public @interface JobActionScope {
}
