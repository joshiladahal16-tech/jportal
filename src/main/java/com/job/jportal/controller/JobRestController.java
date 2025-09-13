package com.job.jportal.controller;


import com.job.jportal.model.job;
import com.job.jportal.service.JobService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/jobs")
public class JobRestController {
    private final JobService jobService;
    public JobRestController(JobService jobService) { this.jobService = jobService; }

    @GetMapping
    public List<job> allJobs() { return jobService.findAll(); }

    @GetMapping("/{id}")
    public ResponseEntity<job> getJob(@PathVariable Long id) {
        return jobService.findById(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<job> createJob(@RequestBody job job) {
        job JobService = null;
        job saved = jobService.save(JobService);
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJob(@PathVariable Long id) {
        jobService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
