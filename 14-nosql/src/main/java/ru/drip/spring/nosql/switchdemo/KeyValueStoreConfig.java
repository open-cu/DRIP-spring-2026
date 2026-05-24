package ru.drip.spring.nosql.switchdemo;

import java.util.concurrent.ConcurrentHashMap;

import ru.drip.spring.nosql.switchdemo.keyvalue.KeyValueDemoNoteRepository;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.keyvalue.core.KeyValueOperations;
import org.springframework.data.keyvalue.core.KeyValueTemplate;
import org.springframework.data.map.MapKeyValueAdapter;
import org.springframework.data.map.repository.config.EnableMapRepositories;

@Profile("keyvalue")
@Configuration
@EnableMapRepositories(basePackageClasses = KeyValueDemoNoteRepository.class)
public class KeyValueStoreConfig {

    @Bean
    KeyValueOperations keyValueOperations() {
        return new KeyValueTemplate(new MapKeyValueAdapter(ConcurrentHashMap.class));
    }

    @Bean
    StoreDescriptor storeDescriptor() {
        return () -> "keyvalue";
    }
}
