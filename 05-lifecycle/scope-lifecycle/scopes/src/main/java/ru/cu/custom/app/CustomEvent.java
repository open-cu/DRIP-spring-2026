package ru.cu.custom.app;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

public class CustomEvent extends ApplicationEvent {
    @Getter
    private final String eventName;

    public CustomEvent(Object source, String eventName) {
        super(source);
        this.eventName = eventName;
    }
}
