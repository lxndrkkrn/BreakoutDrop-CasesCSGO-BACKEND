package org.example.breakoutdrop.Controllers.System;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.breakoutdrop.DTOs.Create.CreateCaseDTO;
import org.example.breakoutdrop.DTOs.Create.CreateCategoryDTO;
import org.example.breakoutdrop.Entities.Case;
import org.example.breakoutdrop.Entities.Category;
import org.example.breakoutdrop.Enums.CategoryType;
import org.example.breakoutdrop.Services.ApplicationServices.OpenCaseService;
import org.example.breakoutdrop.Services.DomainServices.CaseService;
import org.example.breakoutdrop.Services.DomainServices.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@CrossOrigin("*")
@RequestMapping("/breakout-drop/category")
@RequiredArgsConstructor

public class CategoryController {

    private final CaseService caseService;
    private final CategoryService categoryService;

    @PostMapping()
    @PreAuthorize("hasAnyRole('ADMIN', 'SERVICE')")
    public ResponseEntity<?> createCategory(@Valid @RequestBody CreateCategoryDTO createCategoryDTO) {
        Category category = categoryService.createCategory(createCategoryDTO);

        return ResponseEntity.ok(category);
    }

    @DeleteMapping()
    @PreAuthorize("hasAnyRole('ADMIN', 'SERVICE')")
    public ResponseEntity<?> deleteCategory(@Valid @RequestBody Long id) {
        Category category = categoryService.deleteCategory(id);

        return ResponseEntity.accepted().build();
    }

    @PatchMapping("/set-title/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SERVICE')")
    public ResponseEntity<?> setTitleCategory(@Valid @PathVariable Long id, @RequestBody String title) {
        categoryService.setTitleToCategory(id, title);

        return ResponseEntity.accepted().build();
    }

    @PatchMapping("/set-type/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SERVICE')")
    public ResponseEntity<?> setTypeCategory(@Valid @PathVariable Long id, @RequestBody CategoryType categoryType) {
        categoryService.setTypeToCategory(id, categoryType);

        return ResponseEntity.accepted().build();
    }

}
