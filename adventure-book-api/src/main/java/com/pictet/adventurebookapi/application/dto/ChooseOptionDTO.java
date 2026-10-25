package com.pictet.adventurebookapi.application.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ChooseOptionDTO {
    @NotNull
    private Integer gotoId;
}
