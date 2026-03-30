package org.example.breakoutdrop.Controllers.Game;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.breakoutdrop.DTOs.Balance.Open.OpeningCaseDTO;
import org.example.breakoutdrop.Entities.Skin;
import org.example.breakoutdrop.Services.ApplicationServices.OpenCaseService;
import org.example.breakoutdrop.Services.DomainServices.CaseService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/breakout-drop/case")
@RequiredArgsConstructor

public class OpenCaseController {

    private final CaseService caseService;
    private final OpenCaseService openCaseService;

    @PostMapping("/{caseId}")
    //@PreAuthorize("isAuthenticated() and #userId == principal.id")
    public ResponseEntity<?> openCase(@Valid @PathVariable Long caseId, @RequestBody Long userId) {
        OpeningCaseDTO openingCaseDTO = new OpeningCaseDTO(userId, caseId);
        String wonSkin = openCaseService.userOpeningCase(openingCaseDTO);

        return ResponseEntity.ok(wonSkin);
    }

}
