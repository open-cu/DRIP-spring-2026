package demo.kafkademo.controller;

import demo.kafkademo.service.DistributionScenarioService;
import demo.kafkademo.service.DistributionUiStateService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class DistributionUiController {
    private final DistributionScenarioService scenarioService;

    public DistributionUiController(DistributionScenarioService scenarioService) {
        this.scenarioService = scenarioService;
    }

    @GetMapping({"/", "/blocks/kafka-distribution-model"})
    String index(Model model) {
        model.addAttribute("data", scenarioService.pageData());
        return "blocks/kafka-distribution-model/index";
    }

    @GetMapping("/blocks/kafka-distribution-model/state")
    @ResponseBody
    DistributionUiStateService.PageData state() {
        return scenarioService.pageData();
    }

    @PostMapping("/blocks/kafka-distribution-model/events")
    String sendEvent(
            @RequestParam(defaultValue = "order-42") String eventKey,
            @RequestParam(defaultValue = "изменился статус заказа") String payload,
            RedirectAttributes redirectAttributes
    ) {
        scenarioService.sendEvent(eventKey, payload);
        redirectAttributes.addFlashAttribute("message", "Событие отправлено с key = " + eventKey);
        return "redirect:/blocks/kafka-distribution-model";
    }

    @PostMapping("/blocks/kafka-distribution-model/reset")
    String reset() {
        scenarioService.reset();
        return "redirect:/blocks/kafka-distribution-model";
    }
}
