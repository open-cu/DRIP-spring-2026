package ru.cu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.cu.model.ArticleComment;

import java.util.List;

public interface ArticleCommentRepository extends JpaRepository<ArticleComment, Long> {
    List<ArticleComment> findByArticleId(long articleId);
}
