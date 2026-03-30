package org.example.breakoutdrop.Controllers.System;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.breakoutdrop.DTOs.Controller.CategoryIdsRequest;
import org.example.breakoutdrop.DTOs.Create.CreateCaseDTO;
import org.example.breakoutdrop.Entities.Case;
import org.example.breakoutdrop.Services.ApplicationServices.OpenCaseService;
import org.example.breakoutdrop.Services.DomainServices.CaseService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/breakout-drop/case")
@RequiredArgsConstructor

public class CaseController {

    private final CaseService caseService;
    private final OpenCaseService openCaseService;

    @PostMapping("/create")
    //@PreAuthorize("hasAnyRole('ADMIN', 'SERVICE')")
    public ResponseEntity<?> createCase(@Valid @RequestBody CreateCaseDTO createCaseDTO) {
        Case newCase = caseService.createCase(createCaseDTO);

        return ResponseEntity.ok(newCase);
    }

    @DeleteMapping("/delete")
    //@PreAuthorize("hasAnyRole('ADMIN', 'SERVICE')")
    public ResponseEntity<?> deleteCase(@Valid @RequestBody Long id) {
        Case newCase = caseService.deleteCase(id);

        return ResponseEntity.accepted().build();
    }

    @PatchMapping("/set-price/{id}")
    //@PreAuthorize("hasAnyRole('ADMIN', 'SERVICE')")
    public ResponseEntity<?> setPriceCase(@Valid @PathVariable Long id, @RequestBody BigDecimal price) {
        caseService.setPriceToCase(id, price);

        return ResponseEntity.accepted().build();
    }

    @PatchMapping("/set-name/{id}")
    //@PreAuthorize("hasAnyRole('ADMIN', 'SERVICE')")
    public ResponseEntity<?> setNameCase(@Valid @PathVariable Long id, @RequestBody String name) {
        caseService.setNameToCase(id, name);

        return ResponseEntity.accepted().build();
    }

    @PatchMapping("/set-category/{id}")
    //@PreAuthorize("hasAnyRole('ADMIN', 'SERVICE')")
    public ResponseEntity<?> setCategoryCase(@Valid @PathVariable Long id, @RequestBody CategoryIdsRequest categoryIds) {
        caseService.setCategoryToCase(id, categoryIds.categoryIdsRequest());

        return ResponseEntity.accepted().build();
    }

    @PatchMapping("/set-picture/{id}")
    //@PreAuthorize("hasAnyRole('ADMIN', 'SERVICE')")
    public ResponseEntity<?> setPictureCase(@Valid @PathVariable Long id, @RequestBody String url) {
        caseService.setPictureToCase(id, url);

        return ResponseEntity.accepted().build();
    }

}
