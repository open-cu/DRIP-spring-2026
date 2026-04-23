package ru.drip.spring.demo;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(classes = ContextCachingObservationTest.TestApplication.class)
@TestMethodOrder(OrderAnnotation.class)
class ContextCachingObservationTest {

    private static final AtomicInteger CREATION_COUNTER = new AtomicInteger();

    @Autowired
    private ContextProbe contextProbe;

    @Test
    @Order(1)
    void shouldCreateContextOnceForTheFirstTest() {
        assertThat(contextProbe.serial()).isEqualTo(1);
    }

    @Test
    @Order(2)
    void shouldReuseSameContextBeforeItIsDirtied() {
        assertThat(contextProbe.serial()).isEqualTo(1);
    }

    @Test
    @Order(3)
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    void shouldMarkContextAsDirtyOnlyWhenExplicitlyRequested() {
        assertThat(contextProbe.serial()).isEqualTo(1);
    }

    @Test
    @Order(4)
    void shouldRebuildContextAfterDirtiesContext() {
        assertThat(contextProbe.serial()).isEqualTo(2);
    }

    @SpringBootConfiguration(proxyBeanMethods = false)
    @Import(ProbeConfiguration.class)
    static class TestApplication {
    }

    @TestConfiguration(proxyBeanMethods = false)
    static class ProbeConfiguration {

        @Bean
        ContextProbe contextProbe() {
            return new ContextProbe(CREATION_COUNTER.incrementAndGet());
        }
    }

    record ContextProbe(int serial) {
    }
}