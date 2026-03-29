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
@Table(name = "languages")
public class LanguageEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cv_id", nullable = false)
    private Cv cv;

    @Column(nullable = false, length = 80)
    private String languageName;

    @Column(nullable = false, length = 50)
    private String proficiencyLevel;

    public LanguageEntry() {
    }

    public Long getId() {
        return id;
    }

    public Cv getCv() {
        return cv;
    }

    public String getLanguageName() {
        return languageName;
    }

    public String getProficiencyLevel() {
        return proficiencyLevel;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCv(Cv cv) {
        this.cv = cv;
    }

    public void setLanguageName(String languageName) {
        this.languageName = languageName;
    }

    public void setProficiencyLevel(String proficiencyLevel) {
        this.proficiencyLevel = proficiencyLevel;
    }
}
