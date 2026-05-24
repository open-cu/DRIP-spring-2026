package demo.kafkademo.controller;

import demo.kafkademo.service.DelayedScenarioService;
import demo.kafkademo.service.DelayedUiStateService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class DelayedUiController {
    private final DelayedScenarioService scenarioService;

    public DelayedUiController(DelayedScenarioService scenarioService) {
        this.scenarioService = scenarioService;
    }

    @GetMapping({"/", "/blocks/kafka-delayed-processing"})
    String index(Model model) {
        model.addAttribute("data", scenarioService.pageData());
        return "blocks/kafka-delayed-processing/index";
    }

    @GetMapping("/blocks/kafka-delayed-processing/state")
    @ResponseBody
    DelayedUiStateService.PageData state() {
        return scenarioService.pageData();
    }

    @PostMapping("/blocks/kafka-delayed-processing/orders")
    String createOrder(
            @RequestParam(defaultValue = "отложенный заказ") String description,
            @RequestParam(defaultValue = "5") int delaySeconds,
            RedirectAttributes redirectAttributes
    ) {
        scenarioService.createOrder(description, delaySeconds);
        redirectAttributes.addFlashAttribute("message", "Заказ создан со статусом ОЖИДАЕТ");
        return "redirect:/blocks/kafka-delayed-processing";
    }

    @PostMapping("/blocks/kafka-delayed-processing/reset")
    String reset() {
        scenarioService.reset();
        return "redirect:/blocks/kafka-delayed-processing";
    }
}
