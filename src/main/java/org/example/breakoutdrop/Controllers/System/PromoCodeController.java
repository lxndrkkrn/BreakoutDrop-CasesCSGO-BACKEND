package org.example.breakoutdrop.Controllers.System;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.breakoutdrop.DTOs.Create.CreateCaseDTO;
import org.example.breakoutdrop.DTOs.Create.CreatePromoDTO;
import org.example.breakoutdrop.Entities.Case;
import org.example.breakoutdrop.Entities.PromoCode;
import org.example.breakoutdrop.Services.DomainServices.PromoCodeService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/breakout-drop/promo")
@RequiredArgsConstructor

public class PromoCodeController {

    private final PromoCodeService promoCodeService;

    @PostMapping()
    @PreAuthorize("hasAnyRole('ADMIN', 'SERVICE')")
    public ResponseEntity<?> createPromo(@Valid @RequestBody CreatePromoDTO createPromoDTO) {
        PromoCode promoCode = promoCodeService.createPromo(createPromoDTO);

        return ResponseEntity.ok(promoCode);
    }

    @DeleteMapping()
    @PreAuthorize("hasAnyRole('ADMIN', 'SERVICE')")
    public ResponseEntity<?> deletePromo(@Valid @RequestBody Long id) {
        promoCodeService.deletePromo(id);

        return ResponseEntity.accepted().build();
    }

}
