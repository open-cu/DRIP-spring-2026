package ru.cu.custom.scope;

import jakarta.annotation.Nonnull;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;

import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

@SuppressWarnings("NullableProblems")
public class JobActionScopeImpl implements Scope {

    public static final String SCOPE_NAME = "JobAction";

    private final ThreadLocal<Map<String, Object>> scopedBeansHolder = new ThreadLocal<>();

    @Override
    public Object get(@Nonnull String beanName, @Nonnull ObjectFactory<?> objectFactory) {

        var scopedBeans = scopedBeansHolder.get();
        var ctx = JobActionContextHolder.getJobContext();
        if (isNull(ctx)) {
            remove(beanName);
            throw new BeanCreationException("Job context not found!");
        }

        if (nonNull(scopedBeans) && scopedBeans.containsKey(beanName)) {
            return scopedBeans.get(beanName);
        }

        if (isNull(scopedBeans)) {
            scopedBeans = new HashMap<>();
            scopedBeansHolder.set(scopedBeans);
        }
        var bean = objectFactory.getObject();
        scopedBeans.put(beanName, bean);

        return bean;
    }

    @Override
    public Object remove(@Nonnull String beanName) {
        var scopedBeans = scopedBeansHolder.get();
        if (nonNull(scopedBeans)) {
            scopedBeans.remove(beanName);
        }
        return null;
    }

    @Override
    public void registerDestructionCallback(@Nonnull String name, @Nonnull Runnable callback) {

    }

    @Override
    public Object resolveContextualObject(@Nonnull String key) {

        if (!key.equals("jobParams")) {
            throw new IllegalArgumentException("Wrong contextual object key!");
        }

        var ctx = JobActionContextHolder.getJobContext();
        if (isNull(ctx)) {
            throw new BeanCreationException("Job context not found!");
        }

        return ctx.getParams();
    }

    @Override
    public String getConversationId() {
        return null;
    }
}
