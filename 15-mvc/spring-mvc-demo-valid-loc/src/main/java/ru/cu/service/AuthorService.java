package ru.cu.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.cu.converters.AuthorConverter;
import ru.cu.dto.AuthorDto;
import ru.cu.repository.AuthorRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;

    private final AuthorConverter authorConverter;

    @Transactional(readOnly = true)
    public List<AuthorDto> findAll() {
        return authorConverter.toDtoList(authorRepository.findAll());
    }
}
