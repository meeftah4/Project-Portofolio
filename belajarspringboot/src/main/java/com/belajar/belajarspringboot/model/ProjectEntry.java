package com.belajar.belajarspringboot.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "projects")
public class ProjectEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cv_id", nullable = false)
    private Cv cv;

    @Column(nullable = false, length = 160)
    private String projectName;

    @Column(length = 120)
    private String role;

    @Column(length = 200)
    private String techStack;

    @Column(nullable = false, length = 30)
    private String startDate;

    @Column(length = 30)
    private String endDate;

    @Column(length = 250)
    private String projectUrl;

    @Column(columnDefinition = "TEXT")
    private String description;

    public ProjectEntry() {
    }

    public Long getId() {
        return id;
    }

    public Cv getCv() {
        return cv;
    }

    public String getProjectName() {
        return projectName;
    }

    public String getRole() {
        return role;
    }

    public String getTechStack() {
        return techStack;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getProjectUrl() {
        return projectUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCv(Cv cv) {
        this.cv = cv;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setTechStack(String techStack) {
        this.techStack = techStack;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void setProjectUrl(String projectUrl) {
        this.projectUrl = projectUrl;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
