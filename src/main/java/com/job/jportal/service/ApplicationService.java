package com.job.jportal.service;

import com.job.jportal.model.Application;
import com.job.jportal.model.User;
import com.job.jportal.model.job;
import com.job.jportal.repository.ApplicationRepository;
import com.job.jportal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Service
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final UserRepository userRepository;

    @Autowired
    public ApplicationService(ApplicationRepository applicationRepository, UserRepository userRepository) {
        this.applicationRepository = applicationRepository;
        this.userRepository = userRepository;
    }

    public void apply(Long userId, job job, MultipartFile resume) {
        try {
            // Save file in "uploads" directory
            String uploadDir = "uploads/";
            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            String filePath = uploadDir + resume.getOriginalFilename();
            File dest = new File(filePath);
            resume.transferTo(dest);

            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Application application = new Application();
            application.setApplicant(user);
            application.setJob(job);
            application.setResumePath(filePath);

            applicationRepository.save(application);

        } catch (IOException e) {
            throw new RuntimeException("Error saving resume file", e);
        }
    }
}
