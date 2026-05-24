package demo.rabbitmqdemo.blocks.springcore;

import java.util.concurrent.TimeUnit;

import demo.rabbitmqdemo.config.blocks.springcore.TopologyConfig;
import demo.rabbitmqdemo.core.model.Order;
import demo.rabbitmqdemo.testsupport.RequiresDocker;
import demo.rabbitmqdemo.testsupport.TestRabbitProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.test.RabbitListenerTest;
import org.springframework.amqp.rabbit.test.RabbitListenerTestHarness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Блок spring-core: точечные проверки listener-ов через spring-rabbit-test")
@RequiresDocker
@RabbitListenerTest(capture = true)
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
class SpringCoreRabbitListenerHarnessIntegrationTest {

    @DynamicPropertySource
    static void rabbitProps(DynamicPropertyRegistry registry) {
        TestRabbitProperties.register(registry);
    }

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitAdmin rabbitAdmin;

    @Autowired
    private RabbitListenerTestHarness harness;

    @BeforeEach
    void clean() {
        rabbitAdmin.initialize();
        rabbitAdmin.purgeQueue(TopologyConfig.QUEUE_ORDERS, false);
        rabbitAdmin.purgeQueue(TopologyConfig.QUEUE_TEXT_ORDERS, false);
        rabbitAdmin.purgeQueue(TopologyConfig.QUEUE_RPC, false);
    }

    @Test
    void textListenerReceivesPlainString() throws Exception {
        rabbitTemplate.convertAndSend("строка для listener");

        RabbitListenerTestHarness.InvocationData data =
                harness.getNextInvocationDataFor("springCoreTextConsumer", 10, TimeUnit.SECONDS);

        assertThat(data).isNotNull();
        assertThat(data.getArguments()).hasSize(1);
        assertThat(data.getArguments()[0]).isEqualTo("строка для listener");
    }

    @Test
    void orderListenerReceivesDtoAfterJsonConversion() throws Exception {
        Order order = new Order(101L, "DTO через converter");

        rabbitTemplate.convertAndSend(
                TopologyConfig.EXCHANGE_ORDERS,
                TopologyConfig.ROUTING_KEY_ORDER_CREATED,
                order
        );

        RabbitListenerTestHarness.InvocationData data =
                harness.getNextInvocationDataFor("springCoreOrderConsumer", 10, TimeUnit.SECONDS);

        assertThat(data).isNotNull();
        assertThat(data.getArguments()).hasSize(1);
        assertThat(data.getArguments()[0]).isEqualTo(order);
    }
}
