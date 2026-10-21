package com.pictet.adventurebookapi.domain;

import com.pictet.adventurebookapi.domain.handlers.ConsequenceHandler;
import com.pictet.adventurebookapi.domain.model.BookSectionOptionConsequence;
import com.pictet.adventurebookapi.domain.model.Game;
import com.pictet.adventurebookapi.domain.model.OptionConsequenceType;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class OptionConsequenceResolver {
    private final Map<OptionConsequenceType, ConsequenceHandler> handlers;

    public OptionConsequenceResolver(List<ConsequenceHandler> handlers) {
        this.handlers = handlers.stream()
                .collect(Collectors.toMap(ConsequenceHandler::getType, ch -> ch));
    }

    public void apply(Game game, BookSectionOptionConsequence c) {
        if (c == null)
            return;
        Optional.ofNullable(handlers.get(c.getType()))
                .orElseThrow(() -> new IllegalStateException("No handler for " + c.getType()))
                .apply(game, c);
    }
}
