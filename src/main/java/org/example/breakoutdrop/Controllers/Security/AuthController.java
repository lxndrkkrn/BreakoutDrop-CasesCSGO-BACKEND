package org.example.breakoutdrop.Controllers.Security;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.breakoutdrop.DTOs.Auth.LoginDTO;
import org.example.breakoutdrop.DTOs.Auth.RegisterDTO;
import org.example.breakoutdrop.DTOs.Create.CreateUserDTO;
import org.example.breakoutdrop.Enums.UserRole;
import org.example.breakoutdrop.Services.DomainServices.ServerService.AuthService;
import org.example.breakoutdrop.Services.DomainServices.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/auth")
@RequiredArgsConstructor

public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @PreAuthorize("isAnonymous()")
    public ResponseEntity<?> register(@RequestBody RegisterDTO registerDTO, HttpServletResponse response) {
        return authService.register(registerDTO, response);
    }

    @PostMapping("/login")
    @PreAuthorize("isAnonymous()")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO, HttpServletResponse response) {
        return authService.login(loginDTO, response);
    }

    @PostMapping("/logout")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        return authService.logout(response);
    }
}