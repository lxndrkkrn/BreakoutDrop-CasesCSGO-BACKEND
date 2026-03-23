package org.example.breakoutdrop.Controllers.Game;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.breakoutdrop.DTOs.Open.OpeningUpgradeDTO;
import org.example.breakoutdrop.Entities.Skin;
import org.example.breakoutdrop.Services.ApplicationServices.UpgradeService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/breakout-drop/upgrade")
@RequiredArgsConstructor

public class UpgradeController {

    private final UpgradeService upgradeService;

    @PostMapping()
    @PreAuthorize("isAuthenticated() and #openingUpgradeDTO.userId == principal.id")
    public ResponseEntity<?> upgradeSkin(@Valid @RequestBody OpeningUpgradeDTO openingUpgradeDTO) {
        Skin wonSkin = upgradeService.upgradeSkin(openingUpgradeDTO);

        return ResponseEntity.ok(wonSkin);
    }

}
