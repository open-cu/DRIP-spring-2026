package demo.rabbitmqdemo.testsupport;

import java.time.Duration;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.utility.DockerImageName;

public final class RabbitMqContainerSupport {

    private static volatile RabbitMQContainer rabbit;

    private static RabbitMQContainer getRabbit() {
        RabbitMQContainer current = rabbit;
        if (current != null) {
            return current;
        }
        synchronized (RabbitMqContainerSupport.class) {
            if (rabbit == null) {
                rabbit = new RabbitMQContainer(DockerImageName.parse("rabbitmq:3.13-management-alpine"))
                        .withStartupTimeout(Duration.ofSeconds(30));
                rabbit.start();
            }
            return rabbit;
        }
    }

    public static void registerProperties(DynamicPropertyRegistry registry) {
        RabbitMQContainer container = getRabbit();
        registry.add("spring.rabbitmq.host", container::getHost);
        registry.add("spring.rabbitmq.port", container::getAmqpPort);
        registry.add("spring.rabbitmq.username", container::getAdminUsername);
        registry.add("spring.rabbitmq.password", container::getAdminPassword);
        registry.add("spring.rabbitmq.virtual-host", () -> "/");
        registry.add("demo.rabbitmq.management-port", container::getHttpPort);
        registry.add("spring.rabbitmq.listener.simple.missing-queues-fatal", () -> "false");
    }

    private RabbitMqContainerSupport() {
    }
}
