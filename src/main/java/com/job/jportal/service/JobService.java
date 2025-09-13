package com.job.jportal.service;

import com.job.jportal.model.job;

import java.util.List;
import java.util.Optional;

public interface JobService {
    job save(job job);

    Optional<job> findById(Long id);

    List<job> findByEmployerUsername(String employerUsername); // ✅ add this

    List<job> findAll();

    void deleteById(Long id);
}
