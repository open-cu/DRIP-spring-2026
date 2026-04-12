package demo.rabbitmqdemo.config.slide14;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("slide14")
@Configuration
public class FanoutExchangeConfig {

    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange("notifications.fanout");
    }

    @Bean
    public Queue notificationMoscowQueue() {
            return QueueBuilder.durable("notifications.moscow.queue").build();
    }
    @Bean
    public Queue notificationPscovQueue() {
        return QueueBuilder.durable("notifications.pscov.queue").build();
    }
    @Bean
    public Queue notificationTverQueue() {
        return QueueBuilder.durable("notifications.tver.queue").build();
    }
    @Bean
    public Queue notificationElistaQueue() {
        return QueueBuilder.durable("notifications.elista.queue").build();
    }

    @Bean
    public Binding fanoutBinding1() {
        return BindingBuilder.bind(notificationMoscowQueue())
                .to(fanoutExchange());
    }
    @Bean
    public Binding fanoutBinding2() {
        return BindingBuilder.bind(notificationPscovQueue())
                .to(fanoutExchange());
    }
    @Bean
    public Binding fanoutBinding3() {
        return BindingBuilder.bind(notificationTverQueue())
                .to(fanoutExchange());
    }
    @Bean
    public Binding fanoutBinding4() {
        return BindingBuilder.bind(notificationElistaQueue())
                .to(fanoutExchange());
    }
}
