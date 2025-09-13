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

    public UserServiceImpl(UserRepository userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public User registerNewUser(UserDTO dto) {
        if (userRepo.findByEmail(dto.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        User user = new User();
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));

        // Use role from DTO or fallback to JOBSEEKER
        Role role = dto.getRole() == null ? Role.JOBSEEKER : dto.getRole();
        user.setRole(role);

        return userRepo.save(user);
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
