package org.example.breakoutdrop.Controllers.System;

import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.breakoutdrop.DTOs.Create.CreateSkinDTO;
import org.example.breakoutdrop.Entities.Skin;
import org.example.breakoutdrop.Services.DomainServices.SkinService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/breakout-drop/skin")
@RequiredArgsConstructor

public class SkinController {

    private final SkinService skinService;

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SERVICE')")
    public ResponseEntity<?> findSkin(@Valid @RequestBody Long id) {
        Skin skin = skinService.findSkinById(id);

        return ResponseEntity.ok(skin);
    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ADMIN', 'SERVICE')")
    public ResponseEntity<?> createSkin(@Valid @RequestBody CreateSkinDTO createSkinDTO) {
        Skin skin = skinService.createSkin(createSkinDTO);

        return ResponseEntity.ok(skin);
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasAnyRole('ADMIN', 'SERVICE')")
    public ResponseEntity<?> deleteSkin(@Valid @RequestBody Long id) {
        skinService.deleteSkin(id);

        return ResponseEntity.accepted().build();
    }

    @PatchMapping("/add-to-cases/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SERVICE')")
    public ResponseEntity<?> addSkinInCases(@Valid @PathVariable Long id, @RequestBody List<Long> caseIds) {
        skinService.addSkinInCases(id, caseIds);

        return ResponseEntity.accepted().build();
    }

    @PatchMapping("/remove-from-cases/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SERVICE')")
    public ResponseEntity<?> removeSkinInCases(@Valid @PathVariable Long id, @RequestBody List<Long> caseIds) {
        skinService.removeSkinInCase(id, caseIds);

        return ResponseEntity.accepted().build();
    }

    @PatchMapping("/set-chance/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SERVICE')")
    public ResponseEntity<?> setChance(@Valid @PathVariable Long id, @RequestBody Double chance) {
        skinService.setChance(id, chance);

        return ResponseEntity.accepted().build();
    }

    @PatchMapping("/set-price/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SERVICE')")
    public ResponseEntity<?> setPrice(@Valid @PathVariable Long id, @RequestBody BigDecimal price) {
        skinService.setPrice(id, price);

        return ResponseEntity.accepted().build();
    }

}
