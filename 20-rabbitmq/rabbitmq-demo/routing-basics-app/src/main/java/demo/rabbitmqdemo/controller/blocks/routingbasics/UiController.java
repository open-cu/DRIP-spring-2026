package demo.rabbitmqdemo.controller.blocks.routingbasics;

import demo.rabbitmqdemo.service.blocks.routingbasics.ScenarioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class UiController {
    private final ScenarioService scenarioService;

    public UiController(ScenarioService scenarioService) {
        this.scenarioService = scenarioService;
    }

    @GetMapping({"/", "/blocks/routing-basics"})
    public String page(Model model) {
        if (!model.containsAttribute("directRoutingKey")) {
            model.addAttribute("directRoutingKey", "order.created");
        }
        if (!model.containsAttribute("directDescription")) {
            model.addAttribute("directDescription", "сообщение direct");
        }
        if (!model.containsAttribute("topicRoutingKey")) {
            model.addAttribute("topicRoutingKey", "order.created");
        }
        if (!model.containsAttribute("topicDescription")) {
            model.addAttribute("topicDescription", "сообщение topic");
        }
        if (!model.containsAttribute("fanoutDescription")) {
            model.addAttribute("fanoutDescription", "сообщение fanout");
        }
        if (!model.containsAttribute("headersPriority")) {
            model.addAttribute("headersPriority", "HIGH");
        }
        if (!model.containsAttribute("headersDescription")) {
            model.addAttribute("headersDescription", "сообщение headers");
        }
        model.addAttribute("queueStats", scenarioService.queueStats());
        return "blocks/routing-basics/index";
    }

    @PostMapping("/blocks/routing-basics/send/direct")
    public String sendDirect(@RequestParam("routingKey") String routingKey,
                             @RequestParam("description") String description,
                             RedirectAttributes redirectAttributes) {
        scenarioService.sendDirect(routingKey, description);
        redirectAttributes.addFlashAttribute("directRoutingKey", routingKey);
        redirectAttributes.addFlashAttribute("directDescription", description);
        return "redirect:/blocks/routing-basics";
    }

    @PostMapping("/blocks/routing-basics/send/topic")
    public String sendTopic(@RequestParam("routingKey") String routingKey,
                            @RequestParam("description") String description,
                            RedirectAttributes redirectAttributes) {
        scenarioService.sendTopic(routingKey, description);
        redirectAttributes.addFlashAttribute("topicRoutingKey", routingKey);
        redirectAttributes.addFlashAttribute("topicDescription", description);
        return "redirect:/blocks/routing-basics";
    }

    @PostMapping("/blocks/routing-basics/send/fanout")
    public String sendFanout(@RequestParam("description") String description,
                             RedirectAttributes redirectAttributes) {
        scenarioService.sendFanout(description);
        redirectAttributes.addFlashAttribute("fanoutDescription", description);
        return "redirect:/blocks/routing-basics";
    }

    @PostMapping("/blocks/routing-basics/send/headers")
    public String sendHeaders(@RequestParam("priority") String priority,
                              @RequestParam("description") String description,
                              RedirectAttributes redirectAttributes) {
        scenarioService.sendHeaders(priority, description);
        redirectAttributes.addFlashAttribute("headersPriority", priority);
        redirectAttributes.addFlashAttribute("headersDescription", description);
        return "redirect:/blocks/routing-basics";
    }

    @PostMapping("/blocks/routing-basics/reset")
    public String resetQueues() {
        scenarioService.reset();
        return "redirect:/blocks/routing-basics";
    }
}
