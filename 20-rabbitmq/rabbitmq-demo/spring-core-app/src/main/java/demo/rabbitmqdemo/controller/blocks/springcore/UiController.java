package demo.rabbitmqdemo.controller.blocks.springcore;

import demo.rabbitmqdemo.config.blocks.springcore.TopologyConfig;
import demo.rabbitmqdemo.service.blocks.springcore.ScenarioService;
import demo.rabbitmqdemo.service.blocks.springcore.ScenarioService.SendTextMode;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.RedirectView;

@Controller
public class UiController {

    private final ScenarioService scenarioService;

    public UiController(ScenarioService scenarioService) {
        this.scenarioService = scenarioService;
    }

    @GetMapping({"/", "/blocks/spring-core"})
    public String list(Model model) {
        ScenarioService.PageData pageData = scenarioService.pageData();
        model.addAttribute("orders", pageData.orders());
        model.addAttribute("textMessages", pageData.textMessages());
        model.addAttribute("explicitExchange", TopologyConfig.EXCHANGE_ORDERS);
        model.addAttribute("explicitRoutingKey", TopologyConfig.ROUTING_KEY_TEXT_CREATED);
        model.addAttribute("defaultRoutingKey", TopologyConfig.QUEUE_TEXT_ORDERS);
        return "blocks/spring-core/orders-list";
    }

    @GetMapping("/blocks/spring-core/state")
    @ResponseBody
    public ScenarioService.PageData state() {
        return scenarioService.pageData();
    }

    @GetMapping("/blocks/spring-core/new")
    public String createForm() {
        return "blocks/spring-core/order-create";
    }

    @PostMapping("/blocks/spring-core/orders")
    public RedirectView createOrder(@RequestParam("description") String description) {
        scenarioService.createOrder(description);
        return new RedirectView("/blocks/spring-core", true);
    }

    @PostMapping("/blocks/spring-core/orders/send-text")
    public RedirectView sendText(@RequestParam("text") String text,
                                 @RequestParam("mode") SendTextMode mode) {
        scenarioService.sendText(text, mode);
        return new RedirectView("/blocks/spring-core", true);
    }

    // Backward-compatible endpoint used by existing integration tests.
    @PostMapping("/blocks/spring-core/orders/defaults")
    public RedirectView sendTextByDefaults(@RequestParam("text") String text) {
        scenarioService.sendText(text, SendTextMode.DEFAULT);
        return new RedirectView("/blocks/spring-core", true);
    }

    @PostMapping("/blocks/spring-core/rpc")
    public RedirectView queryStatus(@RequestParam("orderId") Long orderId) {
        scenarioService.queryStatus(orderId);
        return new RedirectView("/blocks/spring-core", true);
    }
}
