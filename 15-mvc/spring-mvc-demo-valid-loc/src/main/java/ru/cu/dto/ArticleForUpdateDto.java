package ru.cu.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ArticleForUpdateDto {
    private Long id;

    @NotBlank(message = "{title-should-not-be-blank}")
    @Size(min = 2, max = 50, message = "{title-wrong-length}")
    private String title;

    @NotEmpty(message = "{authors-list-should-not-be-empty}")
    @Size(message = "{authors-wrong-size}", min = 2, max = 5)
    private List<Long> authors;

    @NotNull(message = "{journal-should-not-be-empty}")
    private Long journal;
}
