package ru.drip.spring.demo;

import java.util.List;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "demo")
public class DemoProperties {

    /**
     * Локаль в виде "ru" / "en".
     */
    private String locale = "en";

    /**
     * Пример сложного свойства: список тегов.
     */
    private List<String> tags = List.of();

    /**
     * Пример сложного свойства: мапа.
     */
    private Map<String, String> meta = Map.of();

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public Map<String, String> getMeta() {
        return meta;
    }

    public void setMeta(Map<String, String> meta) {
        this.meta = meta;
    }
}

