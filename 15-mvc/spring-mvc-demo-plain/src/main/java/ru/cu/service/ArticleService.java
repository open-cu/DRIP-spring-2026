package ru.cu.service;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.cu.converters.ArticleConverter;
import ru.cu.dto.ArticleDto;
import ru.cu.dto.ArticleForUpdateDto;
import ru.cu.repository.ArticleRepository;
import ru.cu.repository.AuthorRepository;
import ru.cu.repository.JournalRepository;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;
import static org.springframework.util.CollectionUtils.isEmpty;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final AuthorRepository authorRepository;
    private final JournalRepository journalRepository;
    private final ArticleRepository articleRepository;

    private final ArticleConverter articleConverter;

    @Transactional(readOnly = true)
    public List<ArticleDto> findAll() {
        return articleRepository.findAll().stream()
                .map(articleConverter::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ArticleDto findById(long id) {
        return articleRepository.findById(id)
                .map(articleConverter::toDto)
                .orElseThrow(() -> new EmptyResultDataAccessException(1));
    }

    @SuppressWarnings("UnusedReturnValue")
    @Transactional
    public ArticleDto save(ArticleForUpdateDto articleDto) {
        if (isNull(articleDto.getJournal())) {
            throw new IllegalArgumentException("Journal missed");
        }

        if (isEmpty(articleDto.getAuthors())) {
            throw new IllegalArgumentException("Authors missed");
        }

        var authors = authorRepository.findAllById(articleDto.getAuthors());
        if (isEmpty(authors) || authors.size() < articleDto.getAuthors().size()) {
            throw new IllegalArgumentException("Any authors not found");
        }

        var journal = journalRepository.findById(articleDto.getJournal())
                .orElseThrow(() -> new IllegalArgumentException("Journal not found"));

        var article = articleConverter.toEntity(articleDto);

        article.setJournal(journal);
        article.setAuthors(authors);
        var savedArticle = articleRepository.save(article);

        return articleConverter.toDto(savedArticle);
    }

    @Transactional
    public void deleteById(long id) {
        articleRepository.deleteById(id);
    }
}
