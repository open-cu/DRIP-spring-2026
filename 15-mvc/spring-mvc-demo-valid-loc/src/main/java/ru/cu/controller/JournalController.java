package ru.cu.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.cu.dto.JournalDto;
import ru.cu.service.JournalService;

import java.util.List;

@SuppressWarnings("unused")
@Controller
@RequiredArgsConstructor
public class JournalController {

    private final JournalService journalService;

    @GetMapping("/journals")
    public String getAllJournals(Model model) {
        List<JournalDto> journals = journalService.findAll();
        model.addAttribute("journals", journals);
        return "journals/list";
    }
}
