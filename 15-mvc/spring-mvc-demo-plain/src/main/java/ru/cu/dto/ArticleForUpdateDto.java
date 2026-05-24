package ru.cu.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ArticleForUpdateDto {
    private Long id;
    private String title;
    private List<Long> authors;
    private Long journal;
}
