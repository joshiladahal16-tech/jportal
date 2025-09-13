package com.job.jportal.controller;

import com.job.jportal.dto.UserDTO;
import com.job.jportal.service.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class AuthController {
    private final UserService userService;
    public AuthController(UserService userService) { this.userService = userService; }

    @GetMapping("/register")
    public String showRegister(Model model) {
        model.addAttribute("userDTO", new UserDTO());
        return "register"; // thymeleaf template
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("userDTO") UserDTO dto, BindingResult br, Model model) {
        if (br.hasErrors()) return "register";
        try {
            userService.registerNewUser(dto);
            return "redirect:/login?registered";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }

    @GetMapping("/login")
    public String login() { return "login"; }

    @GetMapping("/home")
    public String home() { return "index"; }

    @GetMapping("/jobs")
    public String jobs() { return "user-jobs"; }

    @GetMapping("/user/dashboard")
    public String userDashboard() { return "user-dashboard"; }

    @GetMapping("/admin/dashboard")
    public String adminDashboard() { return "admin-dashboard"; }
}
