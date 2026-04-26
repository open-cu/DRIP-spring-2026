package ru.cu.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@SuppressWarnings("unused")
@Controller
@RequiredArgsConstructor
public class JournalController {

    @GetMapping("/journals")
    public String getAllJournals() {
        return "journals/list";
    }
}
