package com.pictet.adventurebookapi.application.controller;

import com.pictet.adventurebookapi.application.dto.ExplorationSectionDTO;
import com.pictet.adventurebookapi.application.dto.ExplorationSectionSummaryDTO;
import com.pictet.adventurebookapi.application.mapper.ExplorationMapper;
import com.pictet.adventurebookapi.domain.service.ExplorationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/books/{bookId}/explore")
public class ExplorationController {

    private final ExplorationService explorationService;
    private final ExplorationMapper explorationMapper;

    @GetMapping
    public List<ExplorationSectionSummaryDTO> getNodes(@PathVariable final Integer bookId) {
        return explorationMapper.toSummaryDTO(explorationService.getSections(bookId));
    }

    @GetMapping("/{sectionId}")
    public ExplorationSectionDTO getNode(@PathVariable final Integer bookId, @PathVariable final Integer sectionId) {
        return explorationMapper.toDTO(explorationService.getSection(bookId, sectionId));
    }

}
