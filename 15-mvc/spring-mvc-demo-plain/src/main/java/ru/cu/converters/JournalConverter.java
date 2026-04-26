package ru.cu.converters;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.cu.dto.JournalDto;
import ru.cu.model.Journal;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class JournalConverter {

    public JournalDto toDto(Journal journal) {
        if (Objects.isNull(journal)) {
            return null;
        }
        return new JournalDto(journal.getId(), journal.getName());
    }

    public List<JournalDto> toDtoList(List<Journal> journals) {
        return journals.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}
