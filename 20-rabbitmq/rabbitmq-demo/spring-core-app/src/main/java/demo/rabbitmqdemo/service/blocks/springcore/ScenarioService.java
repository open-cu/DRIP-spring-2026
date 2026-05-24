package demo.rabbitmqdemo.service.blocks.springcore;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class ScenarioService {
    public enum SendTextMode {
        EXPLICIT,
        DEFAULT
    }

    private final MessagingService messagingService;
    private final UiStateService uiStateService;

    public ScenarioService(MessagingService messagingService, UiStateService uiStateService) {
        this.messagingService = messagingService;
        this.uiStateService = uiStateService;
    }

    public PageData pageData() {
        return new PageData(uiStateService.listOrders(), uiStateService.listTextMessages());
    }

    public void createOrder(String description) {
        messagingService.createAndSendOrder(description);
    }

    public void sendText(String text, SendTextMode mode) {
        messagingService.sendSimpleText(text, mode);
    }

    public void queryStatus(Long orderId) {
        messagingService.requestAndStoreStatus(orderId);
    }

    public record PageData(
            List<UiStateService.OrderView> orders,
            List<UiStateService.TextView> textMessages
    ) {
    }
}
