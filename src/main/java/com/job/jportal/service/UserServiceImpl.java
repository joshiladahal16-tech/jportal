package com.job.jportal.service;

import com.job.jportal.dto.UserDTO;
import com.job.jportal.model.Role;
import com.job.jportal.model.User;
import com.job.jportal.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public UserServiceImpl(UserRepository userRepo, PasswordEncoder passwordEncoder, EmailService emailService) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    @Override
    @Transactional
    public User registerNewUser(UserDTO dto) {
        // Check if email already exists
        if (userRepo.findByEmail(dto.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        // Validate password confirmation if confirmPassword is provided
        if (dto.getConfirmPassword() != null && !dto.getPassword().equals(dto.getConfirmPassword())) {
            throw new RuntimeException("Passwords do not match");
        }

        // Create new user
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        // Use role from DTO or fallback to JOBSEEKER
        Role role = dto.getRole() == null ? Role.JOBSEEKER : dto.getRole();
        user.setRole(role);

        // Save user to database
        User savedUser = userRepo.save(user);

        // Send welcome email
        try {
            emailService.sendWelcomeEmail(savedUser);
        } catch (Exception e) {
            // Log error but don't fail registration
            System.err.println("Failed to send welcome email: " + e.getMessage());
        }

        return savedUser;
    }

    @Override
    public User findByUsername(String email) {
        return userRepo.findByEmail(email).orElse(null);
    }

    @Override
    public List<User> findAll() {
        return userRepo.findAll();
    }
}
