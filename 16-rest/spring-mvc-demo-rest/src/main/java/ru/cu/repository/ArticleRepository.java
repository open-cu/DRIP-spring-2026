package ru.cu.repository;

import jakarta.annotation.Nonnull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.cu.model.Article;

import java.util.List;
import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, Long> {

    @EntityGraph(attributePaths = {"authors", "journal"})
    Optional<Article> findById(long id);

    @EntityGraph(attributePaths = "journal")
    @Nonnull
    List<Article> findAll();
}
