package com.pictet.adventurebookapi.application.mapper;

import com.pictet.adventurebookapi.application.dto.ExplorationSectionDTO;
import com.pictet.adventurebookapi.application.dto.ExplorationSectionSummaryDTO;
import com.pictet.adventurebookapi.domain.model.BookSection;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ExplorationMapper {

    private final ModelMapper modelMapper;

    public ExplorationSectionSummaryDTO toSummaryDTO(final BookSection section) {
        return modelMapper.map(section, ExplorationSectionSummaryDTO.class);
    }

    public List<ExplorationSectionSummaryDTO> toSummaryDTO(final List<BookSection> sections) {
        return sections.stream().map(this::toSummaryDTO).toList();
    }

    public ExplorationSectionDTO toDTO(final BookSection section) {
        final var dto = modelMapper.map(section, ExplorationSectionDTO.class);
        if (dto.getOptions() == null) {
            dto.setOptions(List.of());
        }
        return dto;
    }
}
