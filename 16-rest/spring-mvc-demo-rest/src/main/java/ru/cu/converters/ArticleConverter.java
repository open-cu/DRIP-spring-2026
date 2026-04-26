package ru.cu.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.cu.dto.ArticleDto;
import ru.cu.dto.ArticleForUpdateDto;
import ru.cu.model.Article;

import static java.util.Objects.isNull;

@Component
@RequiredArgsConstructor
public class ArticleConverter {

    private final AuthorConverter authorConverter;

    private final JournalConverter journalConverter;

    public ArticleDto toDto(Article article) {
        if (isNull(article)) {
            return null;
        }
        return new ArticleDto(article.getId(), article.getTitle(),
                authorConverter.toDtoList(article.getAuthors()),
                journalConverter.toDto(article.getJournal())
        );
    }

    public Article toEntity(ArticleForUpdateDto dto) {
        if (isNull(dto)) {
            return null;
        }
        return new Article(isNull(dto.getId()) ? 0 : dto.getId(), dto.getTitle());
    }
}
