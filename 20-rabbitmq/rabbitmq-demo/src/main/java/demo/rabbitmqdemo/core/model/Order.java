package demo.rabbitmqdemo.core.model;

public record Order(Long id, String description) {
    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }
}
