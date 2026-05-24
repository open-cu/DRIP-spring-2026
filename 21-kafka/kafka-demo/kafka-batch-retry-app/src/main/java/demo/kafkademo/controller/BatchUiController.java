package demo.kafkademo.controller;

import demo.kafkademo.service.BatchScenarioService;
import demo.kafkademo.service.BatchUiStateService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class BatchUiController {
    private final BatchScenarioService scenarioService;

    public BatchUiController(BatchScenarioService scenarioService) {
        this.scenarioService = scenarioService;
    }

    @GetMapping({"/", "/blocks/kafka-batch-retry"})
    String index(Model model) {
        model.addAttribute("data", scenarioService.pageData());
        return "blocks/kafka-batch-retry/index";
    }

    @GetMapping("/blocks/kafka-batch-retry/state")
    @ResponseBody
    BatchUiStateService.PageData state() {
        return scenarioService.pageData();
    }

    @PostMapping("/blocks/kafka-batch-retry/publish-batch")
    String publishBatch(@RequestParam(defaultValue = "5") int count, RedirectAttributes redirectAttributes) {
        scenarioService.publishBatch(count);
        redirectAttributes.addFlashAttribute("message", "Пачка событий отправлена");
        return "redirect:/blocks/kafka-batch-retry";
    }

    @PostMapping("/blocks/kafka-batch-retry/publish-failure")
    String publishFailure(RedirectAttributes redirectAttributes) {
        scenarioService.publishFailureBatch();
        redirectAttributes.addFlashAttribute("message", "Пачка с ошибочным событием отправлена");
        return "redirect:/blocks/kafka-batch-retry";
    }

    @PostMapping("/blocks/kafka-batch-retry/reset")
    String reset() {
        scenarioService.reset();
        return "redirect:/blocks/kafka-batch-retry";
    }
}
