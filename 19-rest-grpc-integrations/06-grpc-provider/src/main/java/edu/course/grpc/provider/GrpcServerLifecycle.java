package edu.course.grpc.provider;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.SmartLifecycle;
import org.springframework.stereotype.Component;

@Component
public class GrpcServerLifecycle implements SmartLifecycle, CommandLineRunner {

    private final CourseCatalogGrpcService courseCatalogGrpcService;
    private final int port;
    private Server server;
    private volatile boolean running;

    public GrpcServerLifecycle(
            CourseCatalogGrpcService courseCatalogGrpcService,
            @Value("${grpc.server.port:9090}") int port
    ) {
        this.courseCatalogGrpcService = courseCatalogGrpcService;
        this.port = port;
    }

    @Override
    public void start() {
        try {
            // grpc-java server не является Spring MVC servlet. Мы явно добавляем
            // generated service implementation в ServerBuilder.
            server = ServerBuilder.forPort(port)
                    .addService(courseCatalogGrpcService)
                    .build()
                    .start();
            running = true;
            System.out.println("gRPC provider started on port " + port);
        } catch (IOException ex) {
            throw new IllegalStateException("Could not start gRPC server on port " + port, ex);
        }
    }

    @Override
    public void run(String... args) throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    @Override
    public void stop() {
        if (server != null) {
            // Сначала graceful shutdown, затем принудительная остановка, если RPC
            // не завершились за отведенное время.
            server.shutdown();
            try {
                if (!server.awaitTermination(5, TimeUnit.SECONDS)) {
                    server.shutdownNow();
                }
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                server.shutdownNow();
            }
        }
        running = false;
    }

    @Override
    public void stop(Runnable callback) {
        stop();
        callback.run();
    }

    @Override
    public boolean isRunning() {
        return running;
    }
}
