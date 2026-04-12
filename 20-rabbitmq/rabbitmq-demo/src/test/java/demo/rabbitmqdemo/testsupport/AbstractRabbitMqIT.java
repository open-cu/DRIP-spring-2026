package demo.rabbitmqdemo.testsupport;

import org.awaitility.Awaitility;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.test.RabbitListenerTestHarness;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RequiresDocker
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public abstract class AbstractRabbitMqIT {

    @DynamicPropertySource
    static void rabbitProps(DynamicPropertyRegistry registry) {
        RabbitMqContainerSupport.registerProperties(registry);
        registry.add("spring.rabbitmq.listener.simple.auto-startup", () -> "true");
    }

    protected void purgeQueues(RabbitAdmin rabbitAdmin, String... queueNames) {
        for (String queueName : queueNames) {
            rabbitAdmin.purgeQueue(queueName, false);
        }
    }

    protected <T> T awaitMessage(RabbitTemplate rabbitTemplate, String queueName, Class<T> targetType) {
        final Object[] receivedHolder = new Object[1];

        Awaitility.await()
                .atMost(Duration.ofSeconds(5))
                .untilAsserted(() -> {
                    Object received = rabbitTemplate.receiveAndConvert(queueName);
                    assertNotNull(received, "No message yet in queue " + queueName);
                    assertTrue(targetType.isInstance(received),
                            "Expected " + targetType.getSimpleName() + " but got " + received.getClass().getSimpleName());
                    receivedHolder[0] = received;
                });

        return targetType.cast(receivedHolder[0]);
    }

    protected RabbitListenerTestHarness.InvocationData awaitInvocation(
            RabbitListenerTestHarness harness,
            String listenerId,
            long timeoutSeconds
    ) throws Exception {
        RabbitListenerTestHarness.InvocationData data =
                harness.getNextInvocationDataFor(listenerId, timeoutSeconds, TimeUnit.SECONDS);
        assertNotNull(data, "Listener was not invoked: " + listenerId);
        return data;
    }

    protected void assertNoInvocation(
            RabbitListenerTestHarness harness,
            String listenerId,
            long timeoutSeconds
    ) throws Exception {
        assertNull(harness.getNextInvocationDataFor(listenerId, timeoutSeconds, TimeUnit.SECONDS));
    }
}
