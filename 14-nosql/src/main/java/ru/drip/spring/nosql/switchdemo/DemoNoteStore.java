package ru.drip.spring.nosql.switchdemo;

import java.util.List;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface DemoNoteStore extends ListCrudRepository<DemoNote, String> {

    List<DemoNote> findByOwnerId(String ownerId);
}
