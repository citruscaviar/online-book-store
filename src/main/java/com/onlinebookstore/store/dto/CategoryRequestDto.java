package com.onlinebookstore.store.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class CategoryRequestDto {
    @NotBlank
    private String name;
    private String description;
}
