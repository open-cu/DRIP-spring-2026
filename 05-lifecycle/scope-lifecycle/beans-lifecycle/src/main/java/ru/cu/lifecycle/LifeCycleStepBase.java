package ru.cu.lifecycle;

import jakarta.annotation.Nonnull;
import lombok.Getter;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

public class LifeCycleStepBase implements EnvironmentAware {

    private Environment environment;
    @Getter
    private boolean stepsPrintingRegimeEnabled;

    @Override
    public void setEnvironment(@Nonnull Environment environment) {
        this.environment = environment;
        this.stepsPrintingRegimeEnabled = environment.getProperty("steps.printing.regime.enabled",
                Boolean.class, true);
    }

    public boolean printStepIfNecessary(String step) {
        if (!stepsPrintingRegimeEnabled) {
            return false;
        }
        System.out.println(step);
        return true;
    }

    public boolean isPropertyFalse(String property) {
        return !environment.getProperty(property, Boolean.class, true);
    }

    public boolean isPropertyTrue(String property) {
        return environment.getProperty(property, Boolean.class, true);
    }
}
