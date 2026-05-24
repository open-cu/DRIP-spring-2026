package ru.cu.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.cu.converters.ArticleCommentConverter;
import ru.cu.dto.ArticleCommentDto;
import ru.cu.model.Article;
import ru.cu.model.ArticleComment;
import ru.cu.repository.ArticleCommentRepository;
import ru.cu.repository.ArticleRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArticleCommentService {

    private final ArticleCommentRepository articleCommentRepository;
    private final ArticleRepository articleRepository;
    private final ArticleCommentConverter articleCommentConverter;

    @Transactional(readOnly = true)
    public List<ArticleCommentDto> findByArticleId(long articleId) {
        return articleCommentConverter.toDtoList(articleCommentRepository.findByArticleId(articleId));
    }

    @Transactional
    public ArticleCommentDto create(ArticleCommentDto commentDto) {
        Article article = articleRepository.findById(commentDto.getArticleId())
                .orElseThrow(() -> new EmptyResultDataAccessException(1));

        var comment = articleCommentConverter.toEntity(commentDto);
        comment.setArticle(article);

        var savedComment = articleCommentRepository.save(comment);
        return articleCommentConverter.toDto(savedComment);
    }

    @Transactional
    public void delete(Long id) {
        articleCommentRepository.deleteById(id);
    }
}
