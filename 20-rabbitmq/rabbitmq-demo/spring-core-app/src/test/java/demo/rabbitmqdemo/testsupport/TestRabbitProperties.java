package demo.rabbitmqdemo.testsupport;

import org.springframework.test.context.DynamicPropertyRegistry;

public final class TestRabbitProperties {

    public static void register(DynamicPropertyRegistry registry) {
        RabbitMqContainerSupport.registerProperties(registry);
        registry.add("spring.rabbitmq.listener.simple.auto-startup", () -> "true");
    }

    private TestRabbitProperties() {
    }
}
