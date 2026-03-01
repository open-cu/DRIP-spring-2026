package ru.cu.lecture.config;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.util.StringUtils;

import java.util.Properties;

public class YamlPropertySource extends PropertiesPropertySource {

    public YamlPropertySource(String name, EncodedResource resource)  {
        super(name, getPropertiesFromResource(resource));
    }

    public YamlPropertySource(EncodedResource resource) {
        this(getNameForResource(resource.getResource()), resource);
    }

    private static String getNameForResource(Resource resource) {
        String name = resource.getDescription();
        if (!StringUtils.hasText(name)) {
            name = resource.getClass().getSimpleName() + "@" + System.identityHashCode(resource);
        }
        return name;
    }

    private static Properties getPropertiesFromResource(EncodedResource encodedResource) {
        var factory = new YamlPropertiesFactoryBean();

        var resource = encodedResource.getResource();
        factory.setResources(resource);

        return factory.getObject();
    }
}
