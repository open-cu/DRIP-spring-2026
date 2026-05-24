package ru.drip.spring.nosql.switchdemo;

import org.springframework.data.annotation.Id;
import org.springframework.data.keyvalue.annotation.KeySpace;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@Document("demoNotes")
@RedisHash("demoNotes")
@KeySpace("demoNotes")
public class DemoNote {

    @Id
    private String id;
    @org.springframework.data.redis.core.index.Indexed
    @org.springframework.data.mongodb.core.index.Indexed
    private String ownerId;
    private String label;

    public DemoNote() {
    }

    public DemoNote(String id, String ownerId, String label) {
        this.id = id;
        this.ownerId = ownerId;
        this.label = label;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
