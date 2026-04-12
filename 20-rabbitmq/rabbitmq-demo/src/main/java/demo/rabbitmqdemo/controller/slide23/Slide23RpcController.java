package demo.rabbitmqdemo.controller.slide23;

import demo.rabbitmqdemo.core.model.OrderRequest;
import demo.rabbitmqdemo.core.model.OrderResponse;
import demo.rabbitmqdemo.service.slide23.OrderQueryService;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Profile("slide23")
@RestController
public class Slide23RpcController {

    private final OrderQueryService service;

    public Slide23RpcController(OrderQueryService service) {
        this.service = service;
    }

    @PostMapping("/api/slide23/rpc")
    public OrderResponse rpc(@RequestBody OrderRequest request) {
        return service.sendAndWait(request);
    }
}

