package org.example.breakoutdrop.Controllers.User;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.breakoutdrop.DTOs.Create.CreateUserDTO;
import org.example.breakoutdrop.Entities.User;
import org.example.breakoutdrop.Services.ApplicationServices.OpenCaseService;
import org.example.breakoutdrop.Services.DomainServices.UserService;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/breakout-drop/user")
@RequiredArgsConstructor

public class UserController {

    private final OpenCaseService openCaseService;
    private final UserService userService;

    @GetMapping("/{id}")
    //@PreAuthorize("hasAnyRole('ADMIN', 'MODER')")
    public ResponseEntity<?> findUsers(@Valid @PathVariable Long id) {
        User user = userService.findUserById(id);

        return ResponseEntity.ok(user);
    }

    @PostMapping("/create")
    @PreAuthorize("isAnonymous()")
    public ResponseEntity<?> createUser(@Valid @RequestBody CreateUserDTO createUserDTO) {
        User user = userService.createUser(createUserDTO);

        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/delete/{id}")
    //@PreAuthorize("hasAnyRole('ADMIN', 'MODER')")
    public ResponseEntity<?> deleteUser(@Valid @PathVariable Long id) {
        userService.deleteUser(id);

        return ResponseEntity.accepted().build();
    }

    @PatchMapping("/add-role/{id}")
    //@PreAuthorize("hasAnyRole('ADMIN', 'MODER') or #id == principal.id")
    public ResponseEntity<?> addRole(@Param("id") @Valid @PathVariable Long id, @RequestBody Long roleId) {
        User user = userService.addRole(id, roleId);

        return ResponseEntity.ok(user);
    }

    @PatchMapping("/remove-role/{id}")
    //@PreAuthorize("hasAnyRole('ADMIN', 'MODER') or #id == principal.id")
    public ResponseEntity<?> removeRole(@Param("id") @Valid @PathVariable Long id, @RequestBody Long roleId) {
        User user = userService.deleteRole(id, roleId);

        return ResponseEntity.ok(user);
    }

    @PatchMapping("/change-tradeURL/{id}")
    //@PreAuthorize("hasAnyRole('ADMIN', 'MODER') or #id == principal.id")
    public ResponseEntity<?> changeTradeURL(@Param("id") @Valid @PathVariable Long id, @RequestBody String tradeURL) {
        userService.changeTradeURL(id, tradeURL);

        return ResponseEntity.accepted().build();
    }

    @PatchMapping("/change-password/{id}")
    //@PreAuthorize("hasAnyRole('ADMIN', 'MODER') or #id == principal.id")
    public ResponseEntity<?> changePassword(@Param("id") @Valid @PathVariable Long id, @RequestBody String password) {
        userService.changePassword(id, password);

        return ResponseEntity.accepted().build();
    }

    @PatchMapping("/change-Email/{id}")
    //@PreAuthorize("hasAnyRole('ADMIN', 'MODER') or #id == principal.id")
    public ResponseEntity<?> changeEmail(@Param("id") @Valid @PathVariable Long id, @RequestBody String email) {
        userService.changeEmail(id, email);

        return ResponseEntity.accepted().build();
    }

}
