package com.pictet.adventurebookapi.application.dto;

import lombok.Data;

@Data
public class CreateGameDTO {
    private String playerId;
    private Integer bookId;
}
