package com.pictet.adventurebookapi.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Game {
    private String gameId;
    private GameState gameState;
    private String playerId;
    private Integer bookId;
    private Integer sectionId;
    private int healthPoints;
    private String consequenceText;
}
