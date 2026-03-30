package org.example.breakoutdrop.Controllers.User;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.breakoutdrop.DTOs.Balance.P2pAddBalanceDTO;
import org.example.breakoutdrop.Services.ApplicationServices.OpenCaseService;
import org.example.breakoutdrop.Services.ApplicationServices.ReplenishmentOfBalanceService;
import org.example.breakoutdrop.Services.DomainServices.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@CrossOrigin("*")
@RequestMapping("/breakout-drop/balance")
@RequiredArgsConstructor

public class BalanceController {

    private final OpenCaseService openCaseService;
    private final UserService userService;
    private final ReplenishmentOfBalanceService replenishmentOfBalanceService;

    @PatchMapping("/set-balance/{id}")
    //@PreAuthorize("hasAnyRole('ADMIN', 'SERVICE')")
    public ResponseEntity<?> setBalance(@Valid @PathVariable Long id, @Valid @RequestBody BigDecimal balance) {
        userService.setBalanceToUser(id, balance);

        return ResponseEntity.accepted().build();
    }

    @PostMapping("/add-balance/{id}")
    //@PreAuthorize("hasAnyRole('ADMIN', 'SERVICE')")
    public ResponseEntity<?> addBalance(@Valid @PathVariable Long id, @Valid @RequestBody BigDecimal balance) {
        userService.addBalanceToUser(id, balance);

        return ResponseEntity.accepted().build();
    }

    @PostMapping("/take-balance/{id}")
    //@PreAuthorize("hasAnyRole('ADMIN', 'SERVICE')")
    public ResponseEntity<?> takeBalance(@Valid @PathVariable Long id, @Valid @RequestBody BigDecimal balance) {
        userService.takeBalanceToUser(id, balance);

        return ResponseEntity.accepted().build();
    }

    @PostMapping("/p2p-add-balance/{id}")
    //@PreAuthorize("hasAnyRole('ADMIN', 'SERVICE')")
    public ResponseEntity<?> p2pAddBalance(@Valid @PathVariable Long id, @Valid @RequestBody P2pAddBalanceDTO p2pAddBalanceDTO) {
        replenishmentOfBalanceService.p2pAddBalance(id, p2pAddBalanceDTO);

        return ResponseEntity.accepted().build();
    }

}
