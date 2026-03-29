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
@Table(name = "certificates")
public class CertificateEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cv_id", nullable = false)
    private Cv cv;

    @Column(nullable = false, length = 180)
    private String certificateName;

    @Column(nullable = false, length = 160)
    private String issuer;

    @Column(nullable = false, length = 30)
    private String issueDate;

    @Column(length = 100)
    private String credentialId;

    @Column(length = 250)
    private String credentialUrl;

    public CertificateEntry() {
    }

    public Long getId() {
        return id;
    }

    public Cv getCv() {
        return cv;
    }

    public String getCertificateName() {
        return certificateName;
    }

    public String getIssuer() {
        return issuer;
    }

    public String getIssueDate() {
        return issueDate;
    }

    public String getCredentialId() {
        return credentialId;
    }

    public String getCredentialUrl() {
        return credentialUrl;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCv(Cv cv) {
        this.cv = cv;
    }

    public void setCertificateName(String certificateName) {
        this.certificateName = certificateName;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public void setIssueDate(String issueDate) {
        this.issueDate = issueDate;
    }

    public void setCredentialId(String credentialId) {
        this.credentialId = credentialId;
    }

    public void setCredentialUrl(String credentialUrl) {
        this.credentialUrl = credentialUrl;
    }
}
