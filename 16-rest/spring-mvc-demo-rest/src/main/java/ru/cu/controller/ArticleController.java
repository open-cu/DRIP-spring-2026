package ru.cu.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.cu.service.ArticleService;
import ru.cu.service.AuthorService;
import ru.cu.service.JournalService;

@SuppressWarnings("unused")
@Controller
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;
    private final AuthorService authorService;
    private final JournalService journalService;

    @GetMapping({"/", "/articles"})
    public String getAllArticles() {
        return "articles/list";
    }

    @GetMapping("/articles/new")
    public String showNewArticleForm(Model model) {
        model.addAttribute("action", "create");
        return "articles/edit";
    }

    @GetMapping("/articles/edit/{id}")
    public String showEditArticleForm(@PathVariable Long id, Model model) {
        model.addAttribute("action", "edit");
        model.addAttribute("articleId", id);
        return "articles/edit";
    }

    @GetMapping("/articles/{id}")
    public String getArticleById(@PathVariable Long id, Model model) {
        model.addAttribute("articleId", id);
        return "articles/view";
    }
}
