package com.job.jportal.service;

import com.job.jportal.dto.UserDTO;
import com.job.jportal.model.User;
import java.util.List;

public interface UserService {
    User registerNewUser(UserDTO dto);
    User findByUsername(String username);
    List<User> findAll();   // <-- add this
}
