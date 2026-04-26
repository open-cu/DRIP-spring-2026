package ru.cu.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.cu.dto.ArticleDto;
import ru.cu.dto.ArticleForUpdateDto;
import ru.cu.service.ArticleService;
import ru.cu.service.AuthorService;
import ru.cu.service.JournalService;

import static java.util.Objects.nonNull;

@SuppressWarnings("unused")
@Controller
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;
    private final AuthorService authorService;
    private final JournalService journalService;

    @GetMapping({"/", "/articles"})
    public String getAllArticles(Model model) {
        model.addAttribute("articles", articleService.findAll());
        return "articles/list";
    }

    @GetMapping("/articles/new")
    public String showNewArticleForm(Model model) {
        ArticleDto article = new ArticleDto();
        model.addAttribute("action", "create");
        model.addAttribute("article", article);
        model.addAttribute("authors", authorService.findAll());
        model.addAttribute("journals", journalService.findAll());
        return "articles/edit";
    }

    @GetMapping("/articles/edit/{id}")
    public String showEditArticleForm(@PathVariable Long id, Model model) {
        ArticleDto article = articleService.findById(id);
        model.addAttribute("action", "edit");
        model.addAttribute("article", article);
        model.addAttribute("authors", authorService.findAll());
        model.addAttribute("journals", journalService.findAll());
        return "articles/edit";
    }

    @PostMapping("/articles")
    public String saveArticle(@ModelAttribute ArticleForUpdateDto articleDto) {
        articleService.save(articleDto);
        return "redirect:/articles";
    }

    @GetMapping("/articles/{id}")
    public String getArticleById(@PathVariable Long id, Model model) {
        ArticleDto article = articleService.findById(id);
        if (nonNull(article)) {
            model.addAttribute("article", article);
            return "articles/view";
        }
        return "redirect:/articles";
    }

    @DeleteMapping("/articles/{id}")
    public String deleteArticle(@PathVariable Long id) {
        articleService.deleteById(id);
        return "redirect:/articles";
    }
}
