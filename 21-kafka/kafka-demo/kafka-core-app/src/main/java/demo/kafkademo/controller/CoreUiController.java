package demo.kafkademo.controller;

import demo.kafkademo.service.CoreScenarioService;
import demo.kafkademo.service.CoreUiStateService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CoreUiController {
    private final CoreScenarioService scenarioService;

    public CoreUiController(CoreScenarioService scenarioService) {
        this.scenarioService = scenarioService;
    }

    @GetMapping({"/", "/blocks/kafka-core"})
    String index(Model model) {
        model.addAttribute("data", scenarioService.pageData());
        return "blocks/kafka-core/index";
    }

    @GetMapping("/blocks/kafka-streams")
    String streams(Model model) {
        model.addAttribute("data", scenarioService.pageData());
        return "blocks/kafka-streams/index";
    }

    @GetMapping({"/blocks/kafka-core/state", "/blocks/kafka-streams/state"})
    @ResponseBody
    CoreUiStateService.PageData state() {
        return scenarioService.pageData();
    }

    @PostMapping("/blocks/kafka-core/orders")
    String createOrder(@RequestParam(defaultValue = "") String description, RedirectAttributes redirectAttributes) {
        scenarioService.createOrder(description);
        redirectAttributes.addFlashAttribute("message", "Событие OrderCreated отправлено в Kafka");
        return "redirect:/blocks/kafka-core";
    }

    @PostMapping({"/blocks/kafka-core/streams", "/blocks/kafka-streams/events"})
    String sendToStreams(@RequestParam(defaultValue = "") String description, RedirectAttributes redirectAttributes) {
        scenarioService.sendToStreams(description);
        redirectAttributes.addFlashAttribute("message", "Событие отправлено в Kafka Streams topology");
        return "redirect:/blocks/kafka-streams";
    }

    @PostMapping("/blocks/kafka-core/reset")
    String reset() {
        scenarioService.reset();
        return "redirect:/blocks/kafka-core";
    }

    @PostMapping("/blocks/kafka-streams/reset")
    String resetStreams() {
        scenarioService.reset();
        return "redirect:/blocks/kafka-streams";
    }
}
