package ru.drip.spring.nosql.switchdemo;

import java.util.List;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class DemoRunner implements ApplicationRunner {

    private final DemoNoteService service;
    private final DemoNoteStore noteStore;
    private final StoreDescriptor storeDescriptor;
    private final Environment environment;
    private final ApplicationContext applicationContext;

    public DemoRunner(
            DemoNoteService service,
            DemoNoteStore noteStore,
            StoreDescriptor storeDescriptor,
            Environment environment,
            ApplicationContext applicationContext
    ) {
        this.service = service;
        this.noteStore = noteStore;
        this.storeDescriptor = storeDescriptor;
        this.environment = environment;
        this.applicationContext = applicationContext;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (!environment.getProperty("demo.cli.enabled", Boolean.class, false)) {
            return;
        }

        String ownerId = environment.getProperty("demo.owner-id", "alice");
        List<DemoNote> notes = service.seed(ownerId);

        System.out.println("Store profile = " + storeDescriptor.storeName());
        System.out.println("Repository bean = " + noteStore.getClass().getName());
        System.out.println("Notes for owner '" + ownerId + "' = " + notes.size());
        for (DemoNote note : notes) {
            System.out.println(note.getLabel());
        }

        SpringApplication.exit(applicationContext, (ExitCodeGenerator) () -> 0);
    }
}
