package com.job.jportal.repository;

import com.job.jportal.model.job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<job, Long> {
    // ✅ custom query method
    List<job> findByEmployerUsername(String employerUsername);
}
