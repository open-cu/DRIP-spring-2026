package demo.rabbitmqdemo.controller.slide18;

import org.springframework.amqp.rabbit.connection.Connection;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Profile("slide18")
@RestController
public class Slide18RabbitPingController {

    private final ConnectionFactory connectionFactory;

    public Slide18RabbitPingController(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    @GetMapping("/api/slide18/rabbit/ping")
    public String ping() {
        Connection connection = connectionFactory.createConnection();
        connection.close();
        return "ok";
    }
}

