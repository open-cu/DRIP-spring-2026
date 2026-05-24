package demo.kafkademo.config;

import java.util.Locale;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Produced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrderStreamsTopology {
    @Bean
    KStream<String, String> orderStreamsPipeline(StreamsBuilder builder) {
        KStream<String, String> stream =
                builder.stream(
                        "orders.stream.input",
                        Consumed.with(Serdes.String(), Serdes.String())
                );

        stream.mapValues(value -> {
                    String[] parts = value.split("\\|", 2);
                    String id = parts[0];
                    String description = parts.length == 2 ? parts[1] : value;
                    return id + "|STREAM_ACCEPTED|" + description.toUpperCase(Locale.ROOT);
                })
                .to("orders.stream.result", Produced.with(Serdes.String(), Serdes.String()));
        return stream;
    }
}
