package ru.cu.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@SuppressWarnings("unused")
@Controller
@RequiredArgsConstructor
public class AuthorController {

    @GetMapping("/authors")
    public String getAllAuthors() {
        return "authors/list";
    }
}
