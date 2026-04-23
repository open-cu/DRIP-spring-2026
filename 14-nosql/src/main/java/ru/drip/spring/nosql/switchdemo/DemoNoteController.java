package ru.drip.spring.nosql.switchdemo;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/demo-notes")
public class DemoNoteController {

    private final DemoNoteService service;
    private final StoreDescriptor storeDescriptor;

    public DemoNoteController(DemoNoteService service, StoreDescriptor storeDescriptor) {
        this.service = service;
        this.storeDescriptor = storeDescriptor;
    }

    @PostMapping("/seed")
    public DemoResponse seed(@RequestParam(defaultValue = "alice") String ownerId) {
        return new DemoResponse(storeDescriptor.storeName(), service.seed(ownerId));
    }

    @GetMapping
    public DemoResponse list(@RequestParam(defaultValue = "alice") String ownerId) {
        return new DemoResponse(storeDescriptor.storeName(), service.findByOwnerId(ownerId));
    }

    @DeleteMapping
    public void reset() {
        service.deleteAll();
    }

    public record DemoResponse(String store, List<DemoNote> notes) {
    }
}
