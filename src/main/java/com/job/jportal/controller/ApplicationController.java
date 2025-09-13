package com.job.jportal.controller;


import com.job.jportal.model.job;
import com.job.jportal.service.ApplicationService;
import com.job.jportal.service.JobService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Controller
@RequestMapping("/applications")
public class ApplicationController {

    private final ApplicationService applicationService;
    private final JobService jobService;

    @Autowired
    public ApplicationController(ApplicationService applicationService, JobService jobService) {
        this.applicationService = applicationService;
        this.jobService = jobService;
    }

    @PostMapping("/apply/{jobId}")
    public String applyForJob(@PathVariable Long jobId,
                              @RequestParam("resume") MultipartFile resume,
                              HttpSession session,
                              Model model) {
        Long userId = (Long) session.getAttribute("userId"); // or use SecurityContext
        Optional<job> job = jobService.findById(jobId);

        applicationService.apply(userId, job.orElse(null), resume);

        model.addAttribute("success", "Application submitted!");
        return "redirect:/jobs/" + jobId;
    }
}
