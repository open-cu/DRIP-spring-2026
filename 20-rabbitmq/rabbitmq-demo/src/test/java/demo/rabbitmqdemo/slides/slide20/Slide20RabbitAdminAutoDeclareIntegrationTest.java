package demo.rabbitmqdemo.slides.slide20;

import demo.rabbitmqdemo.testsupport.AbstractRabbitMqIT;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("Slide 20: RabbitAdmin auto-declares topology beans")
@ActiveProfiles("slide20")
@SpringBootTest
@Import(Slide20RabbitAdminAutoDeclareIntegrationTest.TestTopologyConfig.class)
class Slide20RabbitAdminAutoDeclareIntegrationTest extends AbstractRabbitMqIT {

    @Autowired
    private RabbitAdmin rabbitAdmin;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Test
    void declaresQueueExchangeAndBindingWhenConnectionOpens() {
        rabbitTemplate.execute(channel -> null);

        assertNotNull(rabbitAdmin.getQueueProperties("slide20.auto.queue"));

        rabbitTemplate.convertAndSend("slide20.auto.exchange", "slide20.auto", "admin-message");
        Object received = rabbitTemplate.receiveAndConvert("slide20.auto.queue", 5_000);
        assertEquals("admin-message", received);
    }

    @TestConfiguration
    static class TestTopologyConfig {

        @Bean
        DirectExchange slide20AutoExchange() {
            return new DirectExchange("slide20.auto.exchange");
        }

        @Bean
        Queue slide20AutoQueue() {
            return QueueBuilder.durable("slide20.auto.queue").build();
        }

        @Bean
        Binding slide20AutoBinding(Queue slide20AutoQueue, DirectExchange slide20AutoExchange) {
            return BindingBuilder.bind(slide20AutoQueue)
                    .to(slide20AutoExchange)
                    .with("slide20.auto");
        }
    }
}
