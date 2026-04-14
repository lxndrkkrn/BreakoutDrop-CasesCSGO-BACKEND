package org.example.breakoutdrop.Controllers.Game;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.breakoutdrop.DTOs.Balance.Open.OpeningContractDTO;
import org.example.breakoutdrop.Entities.Skin;
import org.example.breakoutdrop.Services.ApplicationServices.UseOfTheContractService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/breakout-drop/contract")
@RequiredArgsConstructor

public class ContractController {

    private final UseOfTheContractService useOfTheContractService;

    @PostMapping()
    @PreAuthorize("isAuthenticated() and #openingContractDTO.userId == principal.id")
    public ResponseEntity<?> useOfTheContract(@Valid @RequestBody OpeningContractDTO openingContractDTO) {
        Long wonSkin = useOfTheContractService.useOfTheContract(openingContractDTO);

        return ResponseEntity.ok(wonSkin);
    }

}
