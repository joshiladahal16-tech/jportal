package com.job.jportal.dto;

import jakarta.validation.constraints.NotBlank;

public class JobDTO {
    @NotBlank private String title;
    @NotBlank private String description;
    private String company;
    private String location;
    private String salaryRange;
    private String category;

    // getters/setters ...
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getCompany() { return company; }
    public void setCompany(String company) { this.company = company; }
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }
    public String getSalaryRange() { return salaryRange; }
    public void setSalaryRange(String salaryRange) { this.salaryRange = salaryRange; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
}
