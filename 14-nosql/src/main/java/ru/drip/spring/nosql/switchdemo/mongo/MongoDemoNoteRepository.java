package ru.drip.spring.nosql.switchdemo.mongo;

import ru.drip.spring.nosql.switchdemo.DemoNote;
import ru.drip.spring.nosql.switchdemo.DemoNoteStore;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface MongoDemoNoteRepository extends DemoNoteStore, MongoRepository<DemoNote, String> {
}
