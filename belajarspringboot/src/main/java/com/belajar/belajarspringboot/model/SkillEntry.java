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
@Table(name = "skills")
public class SkillEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cv_id", nullable = false)
    private Cv cv;

    @Column(nullable = false, length = 120)
    private String skillName;

    @Column(length = 50)
    private String level;

    public SkillEntry() {
    }

    public Long getId() {
        return id;
    }

    public Cv getCv() {
        return cv;
    }

    public String getSkillName() {
        return skillName;
    }

    public String getLevel() {
        return level;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCv(Cv cv) {
        this.cv = cv;
    }

    public void setSkillName(String skillName) {
        this.skillName = skillName;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}
