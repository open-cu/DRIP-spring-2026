package ru.cu.custom.app;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class CustomEventListener implements ApplicationListener<CustomEvent> {

    @EventListener
    public void onApplicationEventViaAnnotation(CustomEvent event) {
        System.out.printf("Через аннотированный @EventListener слушатель получено событие: %s%n",
                event.getEventName());
        System.out.println("В лесу никого нет, а значит и звук был не слышен");
    }

    @Override
    public void onApplicationEvent(CustomEvent event) {
        System.out.printf("Через слушатель реализующий интерфейс ApplicationListener получено событие: %s%n",
                event.getEventName());
        System.out.println("В лесу никого нет, но звук был");
    }
}
