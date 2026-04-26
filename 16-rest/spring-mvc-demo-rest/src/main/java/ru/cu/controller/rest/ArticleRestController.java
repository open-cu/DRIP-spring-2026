package ru.cu.controller.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.cu.dto.ArticleDto;
import ru.cu.dto.ArticleForUpdateDto;
import ru.cu.service.ArticleService;

import java.util.List;

@SuppressWarnings("unused")
@RestController
@RequiredArgsConstructor
public class ArticleRestController {

    private final ArticleService articleService;

    @GetMapping("/api/articles")
    public List<ArticleDto> getAllArticles() {
        return articleService.findAll();
    }

    @GetMapping("/api/articles/{id}")
    public ArticleDto getArticleById(@PathVariable Long id) {
        return articleService.findById(id);
    }

/*
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/api/articles")
    public ArticleDto addArticle(@RequestBody ArticleForUpdateDto articleDto) {
        return articleService.save(articleDto);
    }
*/

    @PostMapping("/api/articles")
    public ResponseEntity<ArticleDto> addArticle(@RequestBody ArticleForUpdateDto articleDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(articleService.save(articleDto));
    }

    @PutMapping("/api/articles/{id}")
    public ArticleDto updateArticle(@PathVariable Long id, @RequestBody ArticleForUpdateDto articleDto) {
        articleDto.setId(id);
        return articleService.save(articleDto);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/api/articles/{id}")
    public void deleteArticle(@PathVariable Long id) {
        articleService.deleteById(id);
    }
}
