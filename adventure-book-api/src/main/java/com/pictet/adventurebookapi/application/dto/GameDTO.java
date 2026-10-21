package com.pictet.adventurebookapi.application.dto;

import com.pictet.adventurebookapi.domain.model.GameState;
import lombok.Data;

@Data
public class GameDTO {
    private String gameId;
    private String playerId;
    private Integer sectionId;
    private int healthPoints;
    private GameState gameState;
    private String consequenceText;
}
