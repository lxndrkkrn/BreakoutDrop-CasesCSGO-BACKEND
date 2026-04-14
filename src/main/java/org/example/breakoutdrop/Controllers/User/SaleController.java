package org.example.breakoutdrop.Controllers.User;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.breakoutdrop.DTOs.Balance.Sell.SellAllSkinsDTO;
import org.example.breakoutdrop.DTOs.Balance.Sell.SellSkinDTO;
import org.example.breakoutdrop.DTOs.Balance.Sell.WithdrawSkinDTO;
import org.example.breakoutdrop.Services.ApplicationServices.SalesService;
import org.example.breakoutdrop.Services.ApplicationServices.WithdrawSkinService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/breakout-drop/sale")
@RequiredArgsConstructor

public class SaleController {

    private final SalesService salesService;
    private final WithdrawSkinService withdrawSkinService;

    @PostMapping("/skin")
    @PreAuthorize("isAuthenticated() and #sellSkinDTO.userId == principal.id")
    public ResponseEntity<?> sellSkin(@Valid @RequestBody SellSkinDTO sellSkinDTO) {
        salesService.sellSkin(sellSkinDTO);

        return ResponseEntity.accepted().build();
    }

    @PostMapping("/skin/all-skins")
    @PreAuthorize("isAuthenticated() and #sellAllSkinsDTO.userId == principal.id")
    public ResponseEntity<?> sellAllSkin(@Valid @RequestBody SellAllSkinsDTO sellAllSkinsDTO) {
        salesService.sellAllSkins(sellAllSkinsDTO);

        return ResponseEntity.accepted().build();
    }

    @PostMapping("/skin/withdraw")
    @PreAuthorize("isAuthenticated() and #withdrawSkinDTO.userId == principal.id")
    public ResponseEntity<?> withdrawSkin(@Valid @RequestBody WithdrawSkinDTO withdrawSkinDTO) {
        withdrawSkinService.withdrawSkin(withdrawSkinDTO);

        return ResponseEntity.accepted().build();
    }

}
