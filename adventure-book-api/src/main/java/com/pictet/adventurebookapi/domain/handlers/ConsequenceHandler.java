package com.pictet.adventurebookapi.domain.handlers;

import com.pictet.adventurebookapi.domain.model.BookSectionOptionConsequence;
import com.pictet.adventurebookapi.domain.model.Game;
import com.pictet.adventurebookapi.domain.model.OptionConsequenceType;

public interface ConsequenceHandler {
    OptionConsequenceType getType();
    void apply(Game game, BookSectionOptionConsequence consequence);
}
