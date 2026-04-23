package ru.drip.spring.nosql.switchdemo.keyvalue;

import ru.drip.spring.nosql.switchdemo.DemoNote;
import ru.drip.spring.nosql.switchdemo.DemoNoteStore;

import org.springframework.data.keyvalue.repository.KeyValueRepository;

public interface KeyValueDemoNoteRepository extends DemoNoteStore, KeyValueRepository<DemoNote, String> {
}
