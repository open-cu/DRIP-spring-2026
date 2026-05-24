package ru.cu.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.cu.dto.ArticleDto;
import ru.cu.dto.ArticleForUpdateDto;
import ru.cu.dto.JournalDto;
import ru.cu.service.ArticleService;
import ru.cu.service.AuthorService;
import ru.cu.service.JournalService;

import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(ArticleController.class)
class ArticleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ArticleService articleService;

    @MockitoBean
    private AuthorService authorService;

    @MockitoBean
    private JournalService journalService;

    @Test
    void getAllArticles_shouldReturnArticlesView() throws Exception {
        var article1 = new ArticleDto(1L, "Article 1", Collections.emptyList(), new JournalDto());
        var article2 = new ArticleDto(2L, "Article 2", Collections.emptyList(), new JournalDto());
        doReturn(Arrays.asList(article1, article2)).when(articleService).findAll();

        mockMvc.perform(get("/articles"))
                .andExpect(status().isOk())
                .andExpect(view().name("articles/list"))
                .andExpect(model().attributeExists("articles"))
                .andExpect(model().attribute("articles", hasSize(2)));
    }

    @Test
    void showNewArticleForm_shouldReturnEditView() throws Exception {
        doReturn(Collections.emptyList()).when(authorService).findAll();
        doReturn(Collections.emptyList()).when(journalService).findAll();

        mockMvc.perform(get("/articles/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("articles/edit"))
                .andExpect(model().attributeExists("action"))
                .andExpect(model().attribute("action", is("create")))
                .andExpect(model().attributeExists("article"))
                .andExpect(model().attributeExists("authors"))
                .andExpect(model().attributeExists("journals"));
    }

    @Test
    void showEditArticleForm_shouldReturnEditView() throws Exception {
        var article = new ArticleDto(1L, "Test Article", Collections.emptyList(), null);
        doReturn(article).when(articleService).findById(1L);
        doReturn(Collections.emptyList()).when(authorService).findAll();
        doReturn(Collections.emptyList()).when(journalService).findAll();

        mockMvc.perform(get("/articles/edit/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("articles/edit"))
                .andExpect(model().attributeExists("action"))
                .andExpect(model().attribute("action", is("edit")))
                .andExpect(model().attributeExists("article"))
                .andExpect(model().attribute("article", notNullValue()))
                .andExpect(model().attributeExists("authors"))
                .andExpect(model().attributeExists("journals"));
    }

    @Test
    void getArticleById_shouldReturnView() throws Exception {
        var article = new ArticleDto(1L, "Test Article", Collections.emptyList(), null);
        doReturn(article).when(articleService).findById(1L);

        mockMvc.perform(get("/articles/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("articles/view"))
                .andExpect(model().attributeExists("article"))
                .andExpect(model().attribute("article", notNullValue()));
    }

    @Test
    void getArticleById_shouldRedirectWhenArticleNotFound() throws Exception {
        doReturn(null).when(articleService).findById(1L);

        mockMvc.perform(get("/articles/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/articles"));
    }

    @Test
    void saveArticle_shouldRedirectToArticles() throws Exception {
        var articleDto = new ArticleForUpdateDto(1L, "Test", Collections.emptyList(), 1L);
        doReturn(new ArticleDto()).when(articleService).save(any(ArticleForUpdateDto.class));

        mockMvc.perform(post("/articles")
                        .flashAttr("article", articleDto))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/articles"));
    }

    @Test
    void deleteArticle_shouldRedirectToArticles() throws Exception {
        mockMvc.perform(delete("/articles/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/articles"));
    }
}
