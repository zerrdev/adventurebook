package com.pictet.adventurebookapi.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateGameDTO {
    @NotBlank
    private String playerId;
    @NotNull
    private Integer bookId;
}
