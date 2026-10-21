package com.pictet.adventurebookapi.domain.handlers.impl;

import com.pictet.adventurebookapi.domain.handlers.ConsequenceHandler;
import com.pictet.adventurebookapi.domain.model.BookSectionOptionConsequence;
import com.pictet.adventurebookapi.domain.model.Game;
import com.pictet.adventurebookapi.domain.model.OptionConsequenceType;
import org.springframework.stereotype.Component;

@Component
public class LoseHealthConsequenceHandler implements ConsequenceHandler {
    @Override
    public OptionConsequenceType getType() {
        return OptionConsequenceType.LOSE_HEALTH;
    }

    @Override
    public void apply(Game game, BookSectionOptionConsequence consequence) {
        game.setHealthPoints(Math.max(0, game.getHealthPoints() - consequence.getValue()));
    }
}
