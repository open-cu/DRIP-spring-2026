package demo.rabbitmqdemo.testsupport;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.utility.DockerImageName;
import com.github.dockerjava.api.model.ExposedPort;
import com.github.dockerjava.api.model.HostConfig;
import com.github.dockerjava.api.model.PortBinding;
import com.github.dockerjava.api.model.Ports;
import java.time.Duration;

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
                        .withStartupTimeout(Duration.ofSeconds(30))
                        .withCreateContainerCmdModifier(cmd -> {
                            HostConfig hostConfig = cmd.getHostConfig() == null ? new HostConfig() : cmd.getHostConfig();
                            hostConfig.withPublishAllPorts(true);
                            hostConfig.withPortBindings(
                                    new PortBinding(Ports.Binding.empty(), new ExposedPort(5672)),
                                    new PortBinding(Ports.Binding.bindPort(15672), new ExposedPort(15672))
                            );
                            cmd.withHostConfig(hostConfig);
                        });
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
