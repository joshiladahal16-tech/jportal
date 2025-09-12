package com.job.jportal.controller;

import com.job.jportal.model.ExperienceLevel;
import com.job.jportal.model.Qualification;
import com.job.jportal.model.Role;
import com.job.jportal.model.User;
import com.job.jportal.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("userRegistrationDto", new RegistrationDto());
        return "register";
    }

    @PostMapping("/register")
    public String registerSubmit(@Valid @ModelAttribute("userRegistrationDto") RegistrationDto dto,
                                 BindingResult bindingResult,
                                 Model model) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            bindingResult.rejectValue("email", "email.exists", "Email already in use");
        }
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            bindingResult.rejectValue("confirmPassword", "password.mismatch", "Passwords do not match");
        }
        if (bindingResult.hasErrors()) {
            return "register";
        }

        User user = new User();
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setQualification(dto.getQualification());
        user.setExperience(dto.getExperience());
        user.setRole(dto.getRole());
        user.setBio(dto.getBio());
        user.setPasswordHash(passwordEncoder.encode(dto.getPassword()));

        userRepository.save(user);

        model.addAttribute("success", "Account created successfully. You can now log in.");
        model.addAttribute("userRegistrationDto", new RegistrationDto());
        return "register";
    }

    public static class RegistrationDto {
        @jakarta.validation.constraints.NotBlank
        private String firstName;
        @jakarta.validation.constraints.NotBlank
        private String lastName;
        @jakarta.validation.constraints.Email
        @jakarta.validation.constraints.NotBlank
        private String email;
        private String phone;
        private Qualification qualification;
        private ExperienceLevel experience;
        private Role role;
        private String bio;
        @jakarta.validation.constraints.NotBlank
        @jakarta.validation.constraints.Size(min = 8)
        private String password;
        @jakarta.validation.constraints.NotBlank
        private String confirmPassword;
        private Boolean agreeToTerms;

        public String getFirstName() { return firstName; }
        public void setFirstName(String firstName) { this.firstName = firstName; }
        public String getLastName() { return lastName; }
        public void setLastName(String lastName) { this.lastName = lastName; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPhone() { return phone; }
        public void setPhone(String phone) { this.phone = phone; }
        public Qualification getQualification() { return qualification; }
        public void setQualification(Qualification qualification) { this.qualification = qualification; }
        public ExperienceLevel getExperience() { return experience; }
        public void setExperience(ExperienceLevel experience) { this.experience = experience; }
        public Role getRole() { return role; }
        public void setRole(Role role) { this.role = role; }
        public String getBio() { return bio; }
        public void setBio(String bio) { this.bio = bio; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        public String getConfirmPassword() { return confirmPassword; }
        public void setConfirmPassword(String confirmPassword) { this.confirmPassword = confirmPassword; }
        public Boolean getAgreeToTerms() { return agreeToTerms; }
        public void setAgreeToTerms(Boolean agreeToTerms) { this.agreeToTerms = agreeToTerms; }
    }
}


