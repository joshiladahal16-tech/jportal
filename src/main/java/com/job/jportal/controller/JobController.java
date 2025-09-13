package com.job.jportal.controller;

import com.job.jportal.model.job;
import com.job.jportal.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@Controller
public class JobController {

    private final JobService jobService;

    @Autowired
    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("jobs", jobService.findAll());
        return "index"; // public job listing
    }

    @GetMapping("/jobs/{id}")
    public String jobDetail(@PathVariable Long id, Model model) {
        Optional<job> job = jobService.findById(id);
        model.addAttribute("job", job);
        return "job-detail"; // details page
    }
}
