package demo.kafkademo.core.model;

public enum OrderStatus {
    CREATED("СОЗДАН"),
    PENDING("ОЖИДАЕТ"),
    PROCESSED("ОБРАБОТАН"),
    FAILED("ОШИБКА"),
    SENT_TO_DLT("DLT");

    private final String label;

    OrderStatus(String label) {
        this.label = label;
    }

    public String label() {
        return label;
    }
}
