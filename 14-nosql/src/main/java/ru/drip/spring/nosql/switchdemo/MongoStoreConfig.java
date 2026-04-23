package ru.drip.spring.nosql.switchdemo;

import ru.drip.spring.nosql.switchdemo.mongo.MongoDemoNoteRepository;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Profile("mongo")
@Configuration
@EnableMongoRepositories(basePackageClasses = MongoDemoNoteRepository.class)
public class MongoStoreConfig {

    @Bean
    StoreDescriptor storeDescriptor() {
        return () -> "mongo";
    }
}
