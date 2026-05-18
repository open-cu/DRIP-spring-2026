package edu.course.grpc.client.config;

import edu.course.grpc.proto.CourseCatalogServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrpcClientConfig {

    @Bean(destroyMethod = "shutdownNow")
    ManagedChannel courseChannel(CourseGrpcProperties properties) {
        // Channel представляет соединение к gRPC provider-у. В production здесь
        // обычно добавляют TLS, service discovery, interceptors и metrics.
        return ManagedChannelBuilder
                .forAddress(properties.host(), properties.port())
                .usePlaintext()
                .build();
    }

    @Bean
    CourseCatalogServiceGrpc.CourseCatalogServiceBlockingStub courseCatalogStub(ManagedChannel channel) {
        // Stub generated из .proto. Бизнес-код лучше изолировать от него adapter-ом.
        return CourseCatalogServiceGrpc.newBlockingStub(channel);
    }
}
