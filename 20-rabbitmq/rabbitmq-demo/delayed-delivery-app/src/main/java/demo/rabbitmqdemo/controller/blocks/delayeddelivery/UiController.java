package demo.rabbitmqdemo.controller.blocks.delayeddelivery;

import java.util.List;

import demo.rabbitmqdemo.service.blocks.delayeddelivery.ScenarioService;
import demo.rabbitmqdemo.service.blocks.delayeddelivery.UiStateService.OrderView;
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

    @GetMapping({"/", "/blocks/delayed-delivery"})
    public String list(Model model) {
        model.addAttribute("orders", scenarioService.listOrders());
        return "blocks/delayed-delivery/index";
    }

    @GetMapping("/blocks/delayed-delivery/orders/state")
    @ResponseBody
    public List<OrderView> state() {
        return scenarioService.listOrders();
    }

    @PostMapping("/blocks/delayed-delivery/orders")
    public RedirectView create(@RequestParam("description") String description,
                               @RequestParam("delaySeconds") int delaySeconds) {
        scenarioService.create(description, delaySeconds);
        return new RedirectView("/blocks/delayed-delivery", true);
    }

    @PostMapping("/blocks/delayed-delivery/reset")
    public RedirectView reset() {
        scenarioService.reset();
        return new RedirectView("/blocks/delayed-delivery", true);
    }
}
