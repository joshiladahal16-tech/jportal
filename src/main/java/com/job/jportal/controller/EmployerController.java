package com.job.jportal.controller;

import com.job.jportal.dto.JobDTO;
import com.job.jportal.model.job;
import com.job.jportal.service.JobService;
import com.job.jportal.util.SanitizationUtil;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/employer")
@PreAuthorize("hasRole('EMPLOYER')")
public class EmployerController {

    private final JobService jobService;

    @Autowired
    public EmployerController(JobService jobService) {
        this.jobService = jobService;
    }

    // Employer dashboard
    @GetMapping("/dashboard")
    public String showDashboard(Model model, Authentication authentication) {
        String currentEmployer = authentication.getName();
        List<job> employerJobs = jobService.findByEmployerUsername(currentEmployer);

        model.addAttribute("jobs", employerJobs);
        model.addAttribute("jobCount", employerJobs.size());
        return "employer/dashboard";
    }

    // Show job posting form
    @GetMapping("/post-job")
    public String showPostJobForm(Model model) {
        model.addAttribute("jobDTO", new JobDTO());
        return "employer/post-job";
    }

    // Handle job submission
    @PostMapping("/post-job")
    public String postJob(@Valid @ModelAttribute("jobDTO") JobDTO jobDTO,
                          BindingResult result,
                          RedirectAttributes redirectAttributes,
                          Authentication authentication) {

        if (result.hasErrors()) {
            return "employer/post-job";
        }

        try {
            job job = new job();

            // Sanitize input
            job.setTitle(SanitizationUtil.sanitizePlain(jobDTO.getTitle()));
            job.setDescription(SanitizationUtil.sanitizeWithLineBreaks(jobDTO.getDescription()));
            job.setCompany(SanitizationUtil.sanitizePlain(jobDTO.getCompany()));
            job.setLocation(SanitizationUtil.sanitizePlain(jobDTO.getLocation()));
            job.setSalaryRange(SanitizationUtil.sanitizePlain(jobDTO.getSalaryRange()));
            job.setCategory(SanitizationUtil.sanitizePlain(jobDTO.getCategory()));

            // Set system fields
            job.setEmployerUsername(authentication.getName());
            job.setCreatedDate(LocalDateTime.now());
            job.setActive(true);

            jobService.save(job);

            redirectAttributes.addFlashAttribute("successMessage", "Job posted successfully!");
            return "redirect:/employer/manage-jobs";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error posting job. Please try again.");
            return "redirect:/employer/post-job";
        }
    }

    // Manage jobs
    @GetMapping("/manage-jobs")
    public String manageJobs(Model model, Authentication authentication) {
        String currentEmployer = authentication.getName();
        List<job> employerJobs = jobService.findByEmployerUsername(currentEmployer);
        model.addAttribute("jobs", employerJobs);
        return "employer/manage-jobs";
    }

    // Show edit job form
    @GetMapping("/edit-job/{id}")
    public String showEditJobForm(@PathVariable Long id,
                                  Model model,
                                  Authentication authentication,
                                  RedirectAttributes redirectAttributes) {
        job job = jobService.findById(id).orElse(null); // ✅ unwrap Optional

        if (job == null || !job.getEmployerUsername().equals(authentication.getName())) {
            redirectAttributes.addFlashAttribute("errorMessage", "Unauthorized access or job not found.");
            return "redirect:/employer/manage-jobs";
        }

        JobDTO jobDTO = new JobDTO();
        jobDTO.setTitle(job.getTitle());
        jobDTO.setDescription(job.getDescription());
        jobDTO.setCompany(job.getCompany());
        jobDTO.setLocation(job.getLocation());
        jobDTO.setSalaryRange(job.getSalaryRange());
        jobDTO.setCategory(job.getCategory());

        model.addAttribute("jobDTO", jobDTO);
        model.addAttribute("jobId", id);
        return "employer/edit-job";
    }

    // Handle job update
    @PostMapping("/edit-job/{id}")
    public String updateJob(@PathVariable Long id,
                            @Valid @ModelAttribute("jobDTO") JobDTO jobDTO,
                            BindingResult result,
                            Authentication authentication,
                            RedirectAttributes redirectAttributes,
                            Model model) {

        if (result.hasErrors()) {
            model.addAttribute("jobId", id);
            return "employer/edit-job";
        }

        job existingJob = jobService.findById(id).orElse(null); // ✅ unwrap Optional
        if (existingJob == null || !existingJob.getEmployerUsername().equals(authentication.getName())) {
            redirectAttributes.addFlashAttribute("errorMessage", "Unauthorized access or job not found.");
            return "redirect:/employer/manage-jobs";
        }

        existingJob.setTitle(SanitizationUtil.sanitizePlain(jobDTO.getTitle()));
        existingJob.setDescription(SanitizationUtil.sanitizeWithLineBreaks(jobDTO.getDescription()));
        existingJob.setCompany(SanitizationUtil.sanitizePlain(jobDTO.getCompany()));
        existingJob.setLocation(SanitizationUtil.sanitizePlain(jobDTO.getLocation()));
        existingJob.setSalaryRange(SanitizationUtil.sanitizePlain(jobDTO.getSalaryRange()));
        existingJob.setCategory(SanitizationUtil.sanitizePlain(jobDTO.getCategory()));
        existingJob.setUpdatedDate(LocalDateTime.now());

        jobService.save(existingJob);

        redirectAttributes.addFlashAttribute("successMessage", "Job updated successfully!");
        return "redirect:/employer/manage-jobs";
    }

    // Toggle job status
    @PostMapping("/toggle-job-status/{id}")
    public String toggleJobStatus(@PathVariable Long id,
                                  Authentication authentication,
                                  RedirectAttributes redirectAttributes) {
        job job = jobService.findById(id).orElse(null); // ✅ unwrap Optional

        if (job == null || !job.getEmployerUsername().equals(authentication.getName())) {
            redirectAttributes.addFlashAttribute("errorMessage", "Unauthorized access or job not found.");
            return "redirect:/employer/manage-jobs";
        }

        job.setActive(!job.isActive());
        job.setUpdatedDate(LocalDateTime.now());
        jobService.save(job);

        String status = job.isActive() ? "activated" : "deactivated";
        redirectAttributes.addFlashAttribute("successMessage", "Job " + status + " successfully!");

        return "redirect:/employer/manage-jobs";
    }

    // Delete job
    @PostMapping("/delete-job/{id}")
    public String deleteJob(@PathVariable Long id,
                            Authentication authentication,
                            RedirectAttributes redirectAttributes) {
        job job = jobService.findById(id).orElse(null); // ✅ unwrap Optional

        if (job == null || !job.getEmployerUsername().equals(authentication.getName())) {
            redirectAttributes.addFlashAttribute("errorMessage", "Unauthorized access or job not found.");
            return "redirect:/employer/manage-jobs";
        }

        jobService.deleteById(id); // ✅ use deleteById
        redirectAttributes.addFlashAttribute("successMessage", "Job deleted successfully!");
        return "redirect:/employer/manage-jobs";
    }

    // Job details
    @GetMapping("/job-details/{id}")
    public String showJobDetails(@PathVariable Long id,
                                 Model model,
                                 Authentication authentication,
                                 RedirectAttributes redirectAttributes) {
        job job = jobService.findById(id).orElse(null); // ✅ unwrap Optional

        if (job == null || !job.getEmployerUsername().equals(authentication.getName())) {
            redirectAttributes.addFlashAttribute("errorMessage", "Unauthorized access or job not found.");
            return "redirect:/employer/manage-jobs";
        }

        model.addAttribute("job", job);
        return "employer/job-details";
    }

    // Fallback exception handler
    @ExceptionHandler(Exception.class)
    public String handleException(Exception e, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", "An unexpected error occurred. Please try again.");
        return "redirect:/employer/dashboard";
    }
}
