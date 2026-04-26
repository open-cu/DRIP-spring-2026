package ru.cu.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.cu.converters.JournalConverter;
import ru.cu.dto.JournalDto;
import ru.cu.repository.JournalRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class JournalService {

    private final JournalRepository journalRepository;

    private final JournalConverter journalConverter;

    @Transactional(readOnly = true)
    public List<JournalDto> findAll() {
        return journalConverter.toDtoList(journalRepository.findAll());
    }
}
