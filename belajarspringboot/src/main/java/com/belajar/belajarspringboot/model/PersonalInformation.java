package com.belajar.belajarspringboot.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "personal_information")
public class PersonalInformation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cv_id", nullable = false, unique = true)
    private Cv cv;

    @Column(nullable = false, length = 120)
    private String fullName;

    @Column(nullable = false, length = 120)
    private String email;

    @Column(nullable = false, length = 30)
    private String phoneNumber;

    @Column(nullable = false, length = 200)
    private String addressLocation;

    @Column(nullable = false, length = 200)
    private String linkedInUrl;

    @Column(length = 200)
    private String portfolioUrl;

    @Column(nullable = false, length = 20)
    private String postalCode;

    public PersonalInformation() {
    }

    public Long getId() {
        return id;
    }

    public Cv getCv() {
        return cv;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getAddressLocation() {
        return addressLocation;
    }

    public String getLinkedInUrl() {
        return linkedInUrl;
    }

    public String getPortfolioUrl() {
        return portfolioUrl;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCv(Cv cv) {
        this.cv = cv;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setAddressLocation(String addressLocation) {
        this.addressLocation = addressLocation;
    }

    public void setLinkedInUrl(String linkedInUrl) {
        this.linkedInUrl = linkedInUrl;
    }

    public void setPortfolioUrl(String portfolioUrl) {
        this.portfolioUrl = portfolioUrl;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }
}