package com.job.jportal.service;

import com.job.jportal.model.job;
import com.job.jportal.repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JobServiceImpl implements JobService {

    private final JobRepository jobRepository;

    @Autowired
    public JobServiceImpl(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    @Override
    public job save(job job) {
        return jobRepository.save(job);
    }

    @Override
    public Optional<job> findById(Long id) {
        return jobRepository.findById(id);
    }

    @Override
    public List<job> findByEmployerUsername(String employerUsername) {
        return jobRepository.findByEmployerUsername(employerUsername); // ✅ implementation
    }

    @Override
    public List<job> findAll() {
        return jobRepository.findAll();
    }

    @Override
    public void deleteById(Long id) {
        jobRepository.deleteById(id);
    }
}
