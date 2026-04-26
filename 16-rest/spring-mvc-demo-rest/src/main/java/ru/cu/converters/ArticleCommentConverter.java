package ru.cu.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.cu.dto.ArticleCommentDto;
import ru.cu.model.ArticleComment;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@Component
@RequiredArgsConstructor
public class ArticleCommentConverter {

    public ArticleCommentDto toDto(ArticleComment comment) {
        if (isNull(comment)) {
            return null;
        }
        return new ArticleCommentDto(comment.getId(), comment.getContent(), comment.getArticle().getId());
    }

    public List<ArticleCommentDto> toDtoList(List<ArticleComment> comments) {
        return comments.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public ArticleComment toEntity(ArticleCommentDto dto) {
        if (isNull(dto)) {
            return null;
        }
        return new ArticleComment(isNull(dto.getId()) ? 0 : dto.getId(), dto.getContent());
    }
}
