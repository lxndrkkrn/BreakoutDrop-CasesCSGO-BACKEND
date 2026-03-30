package org.example.breakoutdrop.Controllers.Security;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.breakoutdrop.DTOs.Create.CreateUserDTO;
import org.example.breakoutdrop.Enums.UserRole;
import org.example.breakoutdrop.Services.DomainServices.UserService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/")
@RequiredArgsConstructor

public class AuthController {

    private final UserService userService;

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("user", new CreateUserDTO());
        return "register";
    }

//    @GetMapping("/test")
//    public CreateUserDTO testPage(Model model) {
//        var dto = new CreateUserDTO("name", "wquihdwe2", "sdww@gmail.com", "https://string.com", UserRole.ROLE_USER);
//
//
//        model.addAttribute("user", dto);
//        return dto;
//    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") CreateUserDTO createUserDTO) {
        userService.createUser(createUserDTO);
        return "redirect:/login?success";
    }
}