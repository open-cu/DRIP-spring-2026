package ru.cu.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
public class ArticleDto {
    private Long id;
    private String title;
    private List<AuthorDto> authors;
    private JournalDto journal;

    public ArticleDto() {
         authors = new ArrayList<>();
         journal = new JournalDto();
    }
}
