package demo.rabbitmqdemo.slides.slide27;

import demo.rabbitmqdemo.testsupport.AbstractRabbitMqIT;
import demo.rabbitmqdemo.config.slide27.Slide27OrdersTopologyConfig;
import demo.rabbitmqdemo.slides.slide27.Slide27TestOrdersTopologyConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.listener.RabbitListenerEndpointRegistry;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("TDD-кейс №8: Concurrency настройки работают (concurrency + prefetch)")
@ActiveProfiles("slide27")
@SpringBootTest
@Import({Slide27TestOrdersTopologyConfig.class, Slide27OrdersTopologyConfig.class})
class Slide27ConcurrencyPrefetchIntegrationTest extends AbstractRabbitMqIT {

    @DynamicPropertySource
    static void rabbitProps(DynamicPropertyRegistry registry) {
        registry.add("spring.rabbitmq.listener.simple.prefetch", () -> "1");
    }

    @Autowired
    private RabbitListenerEndpointRegistry registry;

    @Test
    void containerHasExpectedConcurrencyAndPrefetch() {
        var container = registry.getListenerContainer("slide27OrderConsumer");
        assertNotNull(container);
        SimpleMessageListenerContainer simple = (SimpleMessageListenerContainer) container;
        Object concurrentConsumers = ReflectionTestUtils.getField(simple, "concurrentConsumers");
        Object maxConcurrentConsumers = ReflectionTestUtils.getField(simple, "maxConcurrentConsumers");
        Object prefetchCount = ReflectionTestUtils.getField(simple, "prefetchCount");
        assertEquals(3, concurrentConsumers);
        assertEquals(10, maxConcurrentConsumers);
        assertEquals(1, prefetchCount);
    }
}
