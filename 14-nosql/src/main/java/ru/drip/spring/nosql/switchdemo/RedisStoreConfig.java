package ru.drip.spring.nosql.switchdemo;

import ru.drip.spring.nosql.switchdemo.redis.RedisDemoNoteRepository;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@Profile("redis")
@Configuration
@EnableRedisRepositories(basePackageClasses = RedisDemoNoteRepository.class)
public class RedisStoreConfig {

    @Bean
    StoreDescriptor storeDescriptor() {
        return () -> "redis";
    }
}
