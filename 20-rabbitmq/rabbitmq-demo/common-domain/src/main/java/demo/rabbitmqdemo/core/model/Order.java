package demo.rabbitmqdemo.core.model;

import java.io.Serial;
import java.io.Serializable;

public record Order(Long id, String description, boolean failForDemo) implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    public Order(Long id, String description) {
        this(id, description, false);
    }

    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

}
