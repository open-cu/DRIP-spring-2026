package ru.cu.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.cu.dto.AuthorDto;
import ru.cu.model.Author;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Objects;

import static java.util.Objects.isNull;

@Component
@RequiredArgsConstructor
public class AuthorConverter {

    public AuthorDto toDto(Author author) {
        if (isNull(author)) {
            return null;
        }
        return new AuthorDto(author.getId(), author.getName());
    }

    public List<AuthorDto> toDtoList(List<Author> authors) {
        return authors.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
