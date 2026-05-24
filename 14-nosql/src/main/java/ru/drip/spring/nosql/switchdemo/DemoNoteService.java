package ru.drip.spring.nosql.switchdemo;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

@Service
public class DemoNoteService {

    private final DemoNoteStore repository;

    public DemoNoteService(DemoNoteStore repository) {
        this.repository = repository;
    }

    public List<DemoNote> seed(String ownerId) {
        repository.deleteAll();
        repository.save(new DemoNote(UUID.randomUUID().toString(), ownerId, "redis-or-mongo-or-keyvalue"));
        repository.save(new DemoNote(UUID.randomUUID().toString(), ownerId, "spring-data-layer-stays-the-same"));
        repository.save(new DemoNote(UUID.randomUUID().toString(), "other-user", "should-not-appear"));
        return repository.findByOwnerId(ownerId);
    }

    public List<DemoNote> findByOwnerId(String ownerId) {
        return repository.findByOwnerId(ownerId);
    }

    public void deleteAll() {
        repository.deleteAll();
    }
}
