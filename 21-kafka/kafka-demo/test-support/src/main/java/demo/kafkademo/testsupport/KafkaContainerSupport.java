package demo.kafkademo.testsupport;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.testcontainers.kafka.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

public final class KafkaContainerSupport {
    private static final KafkaContainer KAFKA = new KafkaContainer(
            DockerImageName.parse("apache/kafka-native:4.0.0")
    );

    private KafkaContainerSupport() {
    }

    public static void register(DynamicPropertyRegistry registry) {
        KAFKA.start();
        registry.add("spring.kafka.bootstrap-servers", KAFKA::getBootstrapServers);
    }
}
