package com.job.jportal.config;

import com.job.jportal.model.Role;
import com.job.jportal.model.User;
import com.job.jportal.model.job;
import com.job.jportal.repository.UserRepository;
import com.job.jportal.repository.JobRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(UserRepository userRepo,
                                      JobRepository jobRepo,
                                      PasswordEncoder passwordEncoder) {
        return args -> {

            // --- Create admin user if not exists ---
            if (userRepo.findByEmail("admin@jobportal.com").isEmpty()) {
                User admin = new User();
                admin.setEmail("admin@jobportal.com");
                admin.setPassword(passwordEncoder.encode("admin123"));
                admin.setRole(Role.ADMIN);
                userRepo.save(admin);
                System.out.println("✔ Admin user created: admin@jobportal.com / admin123");
            }

            // --- Create employer if not exists ---
            if (userRepo.findByEmail("employer@jobportal.com").isEmpty()) {
                User employer = new User();
                employer.setEmail("employer@jobportal.com");
                employer.setPassword(passwordEncoder.encode("employer123"));
                employer.setRole(Role.EMPLOYER);
                userRepo.save(employer);
                System.out.println("✔ Employer user created: employer@jobportal.com / employer123");
            }

            // --- Create job seeker if not exists ---
            if (userRepo.findByEmail("jobseeker@jobportal.com").isEmpty()) {
                User seeker = new User();
                seeker.setEmail("jobseeker@jobportal.com");
                seeker.setPassword(passwordEncoder.encode("seeker123"));
                seeker.setRole(Role.JOBSEEKER);
                userRepo.save(seeker);
                System.out.println("✔ Jobseeker user created: jobseeker@jobportal.com / seeker123");
            }

            // --- Insert a sample job if DB empty ---
            if (jobRepo.count() == 0) {
                job job = new job();
                job.setTitle("Java Developer");
                job.setDescription("Looking for a Java Developer with Spring Boot experience.");
                job.setCompany("Tech Corp");
                job.setLocation("Remote");
                job.setSalaryRange("₹50,000 - ₹70,000");
                job.setCategory("Software Development");
                jobRepo.save(job);
                System.out.println("✔ Sample job created: Java Developer");
            }
        };
    }
}
