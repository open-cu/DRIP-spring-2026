package demo.rabbitmqdemo.controller.blocks.batchretry;

import demo.rabbitmqdemo.service.blocks.batchretry.ScenarioService;
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

    @GetMapping({"/", "/blocks/batch-retry"})
    public String page(@RequestParam(name = "orderId", required = false) Long orderId, Model model) {
        ScenarioService.PageData pageData = scenarioService.pageData(orderId);
        model.addAttribute("batchSaveCalls", pageData.batchSaveCalls());
        model.addAttribute("batchProcessedCount", pageData.batchProcessedCount());
        model.addAttribute("producerBatchSize", pageData.producerBatchSize());
        model.addAttribute("producerBufferLimit", pageData.producerBufferLimit());
        model.addAttribute("producerTimeoutMs", pageData.producerTimeoutMs());
        model.addAttribute("consumerBatchSize", pageData.consumerBatchSize());
        model.addAttribute("consumerReceiveTimeoutMs", pageData.consumerReceiveTimeoutMs());
        model.addAttribute("retryAttempts", pageData.retryAttempts());
        model.addAttribute("retryMaxAttempts", pageData.retryMaxAttempts());
        model.addAttribute("retryBackoffDelayMs", pageData.retryBackoffDelayMs());
        model.addAttribute("retryBackoffMultiplier", pageData.retryBackoffMultiplier());
        model.addAttribute("selectedOrderId", pageData.selectedOrderId());
        model.addAttribute("retryStatus", pageData.retryStatus());
        return "blocks/batch-retry/index";
    }

    @GetMapping("/blocks/batch-retry/state")
    @ResponseBody
    public ScenarioService.PageData state(@RequestParam(name = "orderId", required = false) Long orderId) {
        return scenarioService.pageData(orderId);
    }

    @PostMapping("/blocks/batch-retry/publish-batch")
    public RedirectView publishBatch(@RequestParam("count") int count) {
        scenarioService.publishBatch(count);
        return new RedirectView("/blocks/batch-retry", true);
    }

    @PostMapping("/blocks/batch-retry/publish-retry")
    public RedirectView publishRetry(@RequestParam("orderId") long orderId,
                                     @RequestParam("description") String description,
                                     @RequestParam(name = "failForDemo", defaultValue = "false") boolean failForDemo) {
        scenarioService.publishRetry(orderId, description, failForDemo);
        return new RedirectView("/blocks/batch-retry?orderId=" + orderId, true);
    }

    @PostMapping("/blocks/batch-retry/reset")
    public RedirectView reset() {
        scenarioService.reset();
        return new RedirectView("/blocks/batch-retry", true);
    }
}
