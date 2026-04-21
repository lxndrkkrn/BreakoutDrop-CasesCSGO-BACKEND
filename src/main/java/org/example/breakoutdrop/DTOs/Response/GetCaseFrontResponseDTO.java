package org.example.breakoutdrop.DTOs.Response;

import org.example.breakoutdrop.Entities.Category;

import java.math.BigDecimal;
import java.util.List;

public record GetCaseFrontResponseDTO(
        List<String> categoryList,
        Long id,
        String name,
        String pictureCase,
        BigDecimal price
) {
}
