package org.example.breakoutdrop.DTOs.Response;

import org.example.breakoutdrop.Entities.Category;
import org.example.breakoutdrop.Entities.Skin;

import java.math.BigDecimal;
import java.util.List;

public record GetCaseResponseDTO(
        List<String> categoryList,
        Long id,
        String name,
        String pictureCase,
        BigDecimal price,
        List<Long> skinList
) {
}
