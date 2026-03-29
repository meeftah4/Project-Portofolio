package com.belajar.belajarspringboot;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.belajar.belajarspringboot.model.CertificateEntry;
import com.belajar.belajarspringboot.model.Cv;
import com.belajar.belajarspringboot.model.CvSummary;
import com.belajar.belajarspringboot.model.Education;
import com.belajar.belajarspringboot.model.LanguageEntry;
import com.belajar.belajarspringboot.model.PersonalInformation;
import com.belajar.belajarspringboot.model.ProjectEntry;
import com.belajar.belajarspringboot.model.SkillEntry;
import com.belajar.belajarspringboot.model.UserAccount;
import com.belajar.belajarspringboot.model.WorkExperience;
import com.belajar.belajarspringboot.repository.CertificateEntryRepository;
import com.belajar.belajarspringboot.repository.CvRepository;
import com.belajar.belajarspringboot.repository.CvSummaryRepository;
import com.belajar.belajarspringboot.repository.EducationRepository;
import com.belajar.belajarspringboot.repository.LanguageEntryRepository;
import com.belajar.belajarspringboot.repository.PersonalInformationRepository;
import com.belajar.belajarspringboot.repository.ProjectEntryRepository;
import com.belajar.belajarspringboot.repository.SkillEntryRepository;
import com.belajar.belajarspringboot.repository.UserAccountRepository;
import com.belajar.belajarspringboot.repository.WorkExperienceRepository;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@RestController
@RequestMapping("/api/cvs")
public class CvController {

    private final CvRepository cvRepository;
    private final PersonalInformationRepository personalInformationRepository;
    private final CvSummaryRepository cvSummaryRepository;
    private final EducationRepository educationRepository;
    private final WorkExperienceRepository workExperienceRepository;
    private final ProjectEntryRepository projectEntryRepository;
    private final CertificateEntryRepository certificateEntryRepository;
    private final SkillEntryRepository skillEntryRepository;
    private final LanguageEntryRepository languageEntryRepository;
    private final UserAccountRepository userAccountRepository;

    public CvController(
            CvRepository cvRepository,
            PersonalInformationRepository personalInformationRepository,
            CvSummaryRepository cvSummaryRepository,
            EducationRepository educationRepository,
            WorkExperienceRepository workExperienceRepository,
            ProjectEntryRepository projectEntryRepository,
            CertificateEntryRepository certificateEntryRepository,
            SkillEntryRepository skillEntryRepository,
            LanguageEntryRepository languageEntryRepository,
            UserAccountRepository userAccountRepository) {
        this.cvRepository = cvRepository;
        this.personalInformationRepository = personalInformationRepository;
        this.cvSummaryRepository = cvSummaryRepository;
        this.educationRepository = educationRepository;
        this.workExperienceRepository = workExperienceRepository;
        this.projectEntryRepository = projectEntryRepository;
        this.certificateEntryRepository = certificateEntryRepository;
        this.skillEntryRepository = skillEntryRepository;
        this.languageEntryRepository = languageEntryRepository;
        this.userAccountRepository = userAccountRepository;
    }

    @PostMapping
    public Map<String, Object> createCv(@Valid @RequestBody CreateCvRequest request) {
        UserAccount currentAccount = getCurrentAccountOrThrow();

        Cv cv = new Cv();
        cv.setOwner(currentAccount);
        cv.setTitle(request.title().trim());

        Cv saved = cvRepository.save(cv);
        return toCvResponse(saved);
    }

    @PutMapping("/{cvId}")
    public Map<String, Object> updateCv(
            @PathVariable Long cvId,
            @Valid @RequestBody CreateCvRequest request) {
        Cv cv = getCvOrThrow(cvId);
        cv.setTitle(request.title().trim());
        return toCvResponse(cvRepository.save(cv));
    }

    @DeleteMapping("/{cvId}")
    public Map<String, Object> deleteCv(@PathVariable Long cvId) {
        Cv cv = getCvOrThrow(cvId);

        personalInformationRepository.deleteByCvId(cvId);
        cvSummaryRepository.deleteByCvId(cvId);
        educationRepository.deleteByCvId(cvId);
        workExperienceRepository.deleteByCvId(cvId);
        projectEntryRepository.deleteByCvId(cvId);
        certificateEntryRepository.deleteByCvId(cvId);
        skillEntryRepository.deleteByCvId(cvId);
        languageEntryRepository.deleteByCvId(cvId);

        cvRepository.delete(cv);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("message", "CV berhasil dihapus");
        result.put("cvId", cvId);
        return result;
    }

    @GetMapping
    public List<Map<String, Object>> getAllCvs() {
        UserAccount currentAccount = getCurrentAccountOrThrow();

        return cvRepository.findByOwnerIdOrderByIdAsc(currentAccount.getId()).stream()
                .map(this::toCvResponse)
                .toList();
    }

    @GetMapping("/{cvId}")
    public Map<String, Object> getCvById(@PathVariable Long cvId) {
        return toCvResponse(getCvOrThrow(cvId));
    }

    @PostMapping("/{cvId}/personal-information")
    public Map<String, Object> savePersonalInformation(
            @PathVariable Long cvId,
            @Valid @RequestBody PersonalInformationRequest request) {

        return upsertPersonalInformation(cvId, request, true);
    }

    @PutMapping("/{cvId}/personal-information")
    public Map<String, Object> updatePersonalInformation(
            @PathVariable Long cvId,
            @Valid @RequestBody PersonalInformationRequest request) {

        return upsertPersonalInformation(cvId, request, false);
    }

    @DeleteMapping("/{cvId}/personal-information")
    public Map<String, Object> deletePersonalInformation(@PathVariable Long cvId) {
        getCvOrThrow(cvId);

        personalInformationRepository.findByCvId(cvId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Personal information belum diisi"));

        personalInformationRepository.deleteByCvId(cvId);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("message", "Personal information berhasil dihapus");
        result.put("cvId", cvId);
        return result;
    }

    @GetMapping("/{cvId}/personal-information")
    public Map<String, Object> getPersonalInformation(@PathVariable Long cvId) {
        getCvOrThrow(cvId);

        PersonalInformation info = personalInformationRepository.findByCvId(cvId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Personal information belum diisi"));
        return toPersonalInformationResponse(cvId, info);
    }

    @PostMapping("/{cvId}/summary")
    public Map<String, Object> saveSummary(
            @PathVariable Long cvId,
            @Valid @RequestBody SummaryRequest request) {

        return upsertSummary(cvId, request, true);
    }

    @PutMapping("/{cvId}/summary")
    public Map<String, Object> updateSummary(
            @PathVariable Long cvId,
            @Valid @RequestBody SummaryRequest request) {

        return upsertSummary(cvId, request, false);
    }

    @DeleteMapping("/{cvId}/summary")
    public Map<String, Object> deleteSummary(@PathVariable Long cvId) {
        getCvOrThrow(cvId);

        cvSummaryRepository.findByCvId(cvId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Summary belum diisi"));

        cvSummaryRepository.deleteByCvId(cvId);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("message", "Summary berhasil dihapus");
        result.put("cvId", cvId);
        return result;
    }

    @GetMapping("/{cvId}/summary")
    public Map<String, Object> getSummary(@PathVariable Long cvId) {
        getCvOrThrow(cvId);

        CvSummary summary = cvSummaryRepository.findByCvId(cvId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Summary belum diisi"));
        return toSummaryResponse(cvId, summary);
    }

    @PostMapping("/{cvId}/educations")
    public Map<String, Object> addEducation(
            @PathVariable Long cvId,
            @Valid @RequestBody EducationRequest request) {

        Cv cv = getCvOrThrow(cvId);

        Education education = new Education();
        education.setCv(cv);
        applyEducation(education, request);

        Education saved = educationRepository.save(education);
        return toEducationResponse(cvId, saved);
    }

    @PutMapping("/{cvId}/educations/{educationId}")
    public Map<String, Object> updateEducation(
            @PathVariable Long cvId,
            @PathVariable Long educationId,
            @Valid @RequestBody EducationRequest request) {

        getCvOrThrow(cvId);

        Education education = educationRepository.findByIdAndCvId(educationId, cvId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Education tidak ditemukan"));

        applyEducation(education, request);
        return toEducationResponse(cvId, educationRepository.save(education));
    }

    @DeleteMapping("/{cvId}/educations/{educationId}")
    public Map<String, Object> deleteEducation(
            @PathVariable Long cvId,
            @PathVariable Long educationId) {

        getCvOrThrow(cvId);

        Education education = educationRepository.findByIdAndCvId(educationId, cvId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Education tidak ditemukan"));

        educationRepository.delete(education);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("message", "Education berhasil dihapus");
        result.put("cvId", cvId);
        result.put("educationId", educationId);
        return result;
    }

    @GetMapping("/{cvId}/educations")
    public List<Map<String, Object>> getEducations(@PathVariable Long cvId) {
        getCvOrThrow(cvId);

        return educationRepository.findByCvIdOrderByIdAsc(cvId).stream()
                .map(education -> toEducationResponse(cvId, education))
                .toList();
    }

    @PostMapping("/{cvId}/work-experiences")
    public Map<String, Object> addWorkExperience(
            @PathVariable Long cvId,
            @Valid @RequestBody WorkExperienceRequest request) {

        Cv cv = getCvOrThrow(cvId);

        WorkExperience workExperience = new WorkExperience();
        workExperience.setCv(cv);
        applyWorkExperience(workExperience, request);

        WorkExperience saved = workExperienceRepository.save(workExperience);
        return toWorkExperienceResponse(cvId, saved);
    }

    @PutMapping("/{cvId}/work-experiences/{workExperienceId}")
    public Map<String, Object> updateWorkExperience(
            @PathVariable Long cvId,
            @PathVariable Long workExperienceId,
            @Valid @RequestBody WorkExperienceRequest request) {

        getCvOrThrow(cvId);

        WorkExperience workExperience = workExperienceRepository.findByIdAndCvId(workExperienceId, cvId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Work experience tidak ditemukan"));

        applyWorkExperience(workExperience, request);
        return toWorkExperienceResponse(cvId, workExperienceRepository.save(workExperience));
    }

    @DeleteMapping("/{cvId}/work-experiences/{workExperienceId}")
    public Map<String, Object> deleteWorkExperience(
            @PathVariable Long cvId,
            @PathVariable Long workExperienceId) {

        getCvOrThrow(cvId);

        WorkExperience workExperience = workExperienceRepository.findByIdAndCvId(workExperienceId, cvId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Work experience tidak ditemukan"));

        workExperienceRepository.delete(workExperience);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("message", "Work experience berhasil dihapus");
        result.put("cvId", cvId);
        result.put("workExperienceId", workExperienceId);
        return result;
    }

    @GetMapping("/{cvId}/work-experiences")
    public List<Map<String, Object>> getWorkExperiences(@PathVariable Long cvId) {
        getCvOrThrow(cvId);

        return workExperienceRepository.findByCvIdOrderByIdAsc(cvId).stream()
                .map(workExperience -> toWorkExperienceResponse(cvId, workExperience))
                .toList();
    }

    @PostMapping("/{cvId}/projects")
    public Map<String, Object> addProject(
            @PathVariable Long cvId,
            @Valid @RequestBody ProjectRequest request) {

        Cv cv = getCvOrThrow(cvId);

        ProjectEntry projectEntry = new ProjectEntry();
        projectEntry.setCv(cv);
        applyProject(projectEntry, request);

        ProjectEntry saved = projectEntryRepository.save(projectEntry);
        return toProjectResponse(cvId, saved);
    }

    @PutMapping("/{cvId}/projects/{projectId}")
    public Map<String, Object> updateProject(
            @PathVariable Long cvId,
            @PathVariable Long projectId,
            @Valid @RequestBody ProjectRequest request) {

        getCvOrThrow(cvId);

        ProjectEntry projectEntry = projectEntryRepository.findByIdAndCvId(projectId, cvId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project tidak ditemukan"));

        applyProject(projectEntry, request);
        return toProjectResponse(cvId, projectEntryRepository.save(projectEntry));
    }

    @DeleteMapping("/{cvId}/projects/{projectId}")
    public Map<String, Object> deleteProject(
            @PathVariable Long cvId,
            @PathVariable Long projectId) {

        getCvOrThrow(cvId);

        ProjectEntry projectEntry = projectEntryRepository.findByIdAndCvId(projectId, cvId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project tidak ditemukan"));

        projectEntryRepository.delete(projectEntry);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("message", "Project berhasil dihapus");
        result.put("cvId", cvId);
        result.put("projectId", projectId);
        return result;
    }

    @GetMapping("/{cvId}/projects")
    public List<Map<String, Object>> getProjects(@PathVariable Long cvId) {
        getCvOrThrow(cvId);

        return projectEntryRepository.findByCvIdOrderByIdAsc(cvId).stream()
                .map(projectEntry -> toProjectResponse(cvId, projectEntry))
                .toList();
    }

    @PostMapping("/{cvId}/certificates")
    public Map<String, Object> addCertificate(
            @PathVariable Long cvId,
            @Valid @RequestBody CertificateRequest request) {

        Cv cv = getCvOrThrow(cvId);

        CertificateEntry certificateEntry = new CertificateEntry();
        certificateEntry.setCv(cv);
        applyCertificate(certificateEntry, request);

        CertificateEntry saved = certificateEntryRepository.save(certificateEntry);
        return toCertificateResponse(cvId, saved);
    }

    @PutMapping("/{cvId}/certificates/{certificateId}")
    public Map<String, Object> updateCertificate(
            @PathVariable Long cvId,
            @PathVariable Long certificateId,
            @Valid @RequestBody CertificateRequest request) {

        getCvOrThrow(cvId);

        CertificateEntry certificateEntry = certificateEntryRepository.findByIdAndCvId(certificateId, cvId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Certificate tidak ditemukan"));

        applyCertificate(certificateEntry, request);
        return toCertificateResponse(cvId, certificateEntryRepository.save(certificateEntry));
    }

    @DeleteMapping("/{cvId}/certificates/{certificateId}")
    public Map<String, Object> deleteCertificate(
            @PathVariable Long cvId,
            @PathVariable Long certificateId) {

        getCvOrThrow(cvId);

        CertificateEntry certificateEntry = certificateEntryRepository.findByIdAndCvId(certificateId, cvId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Certificate tidak ditemukan"));

        certificateEntryRepository.delete(certificateEntry);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("message", "Certificate berhasil dihapus");
        result.put("cvId", cvId);
        result.put("certificateId", certificateId);
        return result;
    }

    @GetMapping("/{cvId}/certificates")
    public List<Map<String, Object>> getCertificates(@PathVariable Long cvId) {
        getCvOrThrow(cvId);

        return certificateEntryRepository.findByCvIdOrderByIdAsc(cvId).stream()
                .map(certificateEntry -> toCertificateResponse(cvId, certificateEntry))
                .toList();
    }

    @PostMapping("/{cvId}/skills")
    public Map<String, Object> addSkill(
            @PathVariable Long cvId,
            @Valid @RequestBody SkillRequest request) {

        Cv cv = getCvOrThrow(cvId);

        SkillEntry skillEntry = new SkillEntry();
        skillEntry.setCv(cv);
        applySkill(skillEntry, request);

        SkillEntry saved = skillEntryRepository.save(skillEntry);
        return toSkillResponse(cvId, saved);
    }

    @PutMapping("/{cvId}/skills/{skillId}")
    public Map<String, Object> updateSkill(
            @PathVariable Long cvId,
            @PathVariable Long skillId,
            @Valid @RequestBody SkillRequest request) {

        getCvOrThrow(cvId);

        SkillEntry skillEntry = skillEntryRepository.findByIdAndCvId(skillId, cvId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Skill tidak ditemukan"));

        applySkill(skillEntry, request);
        return toSkillResponse(cvId, skillEntryRepository.save(skillEntry));
    }

    @DeleteMapping("/{cvId}/skills/{skillId}")
    public Map<String, Object> deleteSkill(
            @PathVariable Long cvId,
            @PathVariable Long skillId) {

        getCvOrThrow(cvId);

        SkillEntry skillEntry = skillEntryRepository.findByIdAndCvId(skillId, cvId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Skill tidak ditemukan"));

        skillEntryRepository.delete(skillEntry);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("message", "Skill berhasil dihapus");
        result.put("cvId", cvId);
        result.put("skillId", skillId);
        return result;
    }

    @GetMapping("/{cvId}/skills")
    public List<Map<String, Object>> getSkills(@PathVariable Long cvId) {
        getCvOrThrow(cvId);

        return skillEntryRepository.findByCvIdOrderByIdAsc(cvId).stream()
                .map(skillEntry -> toSkillResponse(cvId, skillEntry))
                .toList();
    }

    @PostMapping("/{cvId}/languages")
    public Map<String, Object> addLanguage(
            @PathVariable Long cvId,
            @Valid @RequestBody LanguageRequest request) {

        Cv cv = getCvOrThrow(cvId);

        LanguageEntry languageEntry = new LanguageEntry();
        languageEntry.setCv(cv);
        applyLanguage(languageEntry, request);

        LanguageEntry saved = languageEntryRepository.save(languageEntry);
        return toLanguageResponse(cvId, saved);
    }

    @PutMapping("/{cvId}/languages/{languageId}")
    public Map<String, Object> updateLanguage(
            @PathVariable Long cvId,
            @PathVariable Long languageId,
            @Valid @RequestBody LanguageRequest request) {

        getCvOrThrow(cvId);

        LanguageEntry languageEntry = languageEntryRepository.findByIdAndCvId(languageId, cvId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Language tidak ditemukan"));

        applyLanguage(languageEntry, request);
        return toLanguageResponse(cvId, languageEntryRepository.save(languageEntry));
    }

    @DeleteMapping("/{cvId}/languages/{languageId}")
    public Map<String, Object> deleteLanguage(
            @PathVariable Long cvId,
            @PathVariable Long languageId) {

        getCvOrThrow(cvId);

        LanguageEntry languageEntry = languageEntryRepository.findByIdAndCvId(languageId, cvId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Language tidak ditemukan"));

        languageEntryRepository.delete(languageEntry);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("message", "Language berhasil dihapus");
        result.put("cvId", cvId);
        result.put("languageId", languageId);
        return result;
    }

    @GetMapping("/{cvId}/languages")
    public List<Map<String, Object>> getLanguages(@PathVariable Long cvId) {
        getCvOrThrow(cvId);

        return languageEntryRepository.findByCvIdOrderByIdAsc(cvId).stream()
                .map(languageEntry -> toLanguageResponse(cvId, languageEntry))
                .toList();
    }

    private Map<String, Object> upsertPersonalInformation(
            Long cvId,
            PersonalInformationRequest request,
            boolean createIfMissing) {

        Cv cv = getCvOrThrow(cvId);

        PersonalInformation info = personalInformationRepository.findByCvId(cvId).orElseGet(() -> {
            if (!createIfMissing) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Personal information belum diisi");
            }
            return new PersonalInformation();
        });

        info.setCv(cv);
        info.setFullName(request.fullName().trim());
        info.setEmail(request.email().trim());
        info.setPhoneNumber(request.phoneNumber().trim());
        info.setAddressLocation(request.addressLocation().trim());
        info.setLinkedInUrl(request.linkedInUrl().trim());
        info.setPortfolioUrl(trimToNull(request.portfolioUrl()));
        info.setPostalCode(request.postalCode().trim());

        PersonalInformation saved = personalInformationRepository.save(info);
        return toPersonalInformationResponse(cvId, saved);
    }

    private Map<String, Object> upsertSummary(
            Long cvId,
            SummaryRequest request,
            boolean createIfMissing) {

        Cv cv = getCvOrThrow(cvId);

        CvSummary summary = cvSummaryRepository.findByCvId(cvId).orElseGet(() -> {
            if (!createIfMissing) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Summary belum diisi");
            }
            return new CvSummary();
        });

        summary.setCv(cv);
        summary.setContent(request.content().trim());

        CvSummary saved = cvSummaryRepository.save(summary);
        return toSummaryResponse(cvId, saved);
    }

    private Cv getCvOrThrow(Long cvId) {
        UserAccount currentAccount = getCurrentAccountOrThrow();

        return cvRepository.findByIdAndOwnerId(cvId, currentAccount.getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "CV tidak ditemukan"));
    }

    private UserAccount getCurrentAccountOrThrow() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Silakan login");
        }

        Object principal = authentication.getPrincipal();
        if (!(principal instanceof UserDetails userDetails)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Silakan login");
        }

        return userAccountRepository.findByEmailIgnoreCase(userDetails.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Akun tidak ditemukan"));
    }

    private void applyEducation(Education education, EducationRequest request) {
        education.setInstitution(request.institution().trim());
        education.setDegree(request.degree().trim());
        education.setFieldOfStudy(request.fieldOfStudy().trim());
        education.setStartDate(request.startDate().trim());
        education.setEndDate(trimToNull(request.endDate()));
        education.setGpa(trimToNull(request.gpa()));
        education.setDescription(trimToNull(request.description()));
    }

    private void applyWorkExperience(WorkExperience workExperience, WorkExperienceRequest request) {
        workExperience.setCompany(request.company().trim());
        workExperience.setPosition(request.position().trim());
        workExperience.setLocation(trimToNull(request.location()));
        workExperience.setStartDate(request.startDate().trim());
        workExperience.setEndDate(trimToNull(request.endDate()));
        workExperience.setCurrentlyWorking(Boolean.TRUE.equals(request.currentlyWorking()));
        workExperience.setDescription(trimToNull(request.description()));
    }

    private void applyProject(ProjectEntry projectEntry, ProjectRequest request) {
        projectEntry.setProjectName(request.projectName().trim());
        projectEntry.setRole(trimToNull(request.role()));
        projectEntry.setTechStack(trimToNull(request.techStack()));
        projectEntry.setStartDate(request.startDate().trim());
        projectEntry.setEndDate(trimToNull(request.endDate()));
        projectEntry.setProjectUrl(trimToNull(request.projectUrl()));
        projectEntry.setDescription(trimToNull(request.description()));
    }

    private void applyCertificate(CertificateEntry certificateEntry, CertificateRequest request) {
        certificateEntry.setCertificateName(request.certificateName().trim());
        certificateEntry.setIssuer(request.issuer().trim());
        certificateEntry.setIssueDate(request.issueDate().trim());
        certificateEntry.setCredentialId(trimToNull(request.credentialId()));
        certificateEntry.setCredentialUrl(trimToNull(request.credentialUrl()));
    }

    private void applySkill(SkillEntry skillEntry, SkillRequest request) {
        skillEntry.setSkillName(request.skillName().trim());
        skillEntry.setLevel(trimToNull(request.level()));
    }

    private void applyLanguage(LanguageEntry languageEntry, LanguageRequest request) {
        languageEntry.setLanguageName(request.languageName().trim());
        languageEntry.setProficiencyLevel(request.proficiencyLevel().trim());
    }

    private String trimToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    private Map<String, Object> toCvResponse(Cv cv) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("id", cv.getId());
        result.put("title", cv.getTitle());
        return result;
    }

    private Map<String, Object> toPersonalInformationResponse(Long cvId, PersonalInformation info) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("id", info.getId());
        result.put("cvId", cvId);
        result.put("fullName", info.getFullName());
        result.put("email", info.getEmail());
        result.put("phoneNumber", info.getPhoneNumber());
        result.put("addressLocation", info.getAddressLocation());
        result.put("linkedInUrl", info.getLinkedInUrl());
        result.put("portfolioUrl", info.getPortfolioUrl());
        result.put("postalCode", info.getPostalCode());
        return result;
    }

    private Map<String, Object> toSummaryResponse(Long cvId, CvSummary summary) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("id", summary.getId());
        result.put("cvId", cvId);
        result.put("content", summary.getContent());
        return result;
    }

    private Map<String, Object> toEducationResponse(Long cvId, Education education) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("id", education.getId());
        result.put("cvId", cvId);
        result.put("institution", education.getInstitution());
        result.put("degree", education.getDegree());
        result.put("fieldOfStudy", education.getFieldOfStudy());
        result.put("startDate", education.getStartDate());
        result.put("endDate", education.getEndDate());
        result.put("gpa", education.getGpa());
        result.put("description", education.getDescription());
        return result;
    }

    private Map<String, Object> toWorkExperienceResponse(Long cvId, WorkExperience workExperience) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("id", workExperience.getId());
        result.put("cvId", cvId);
        result.put("company", workExperience.getCompany());
        result.put("position", workExperience.getPosition());
        result.put("location", workExperience.getLocation());
        result.put("startDate", workExperience.getStartDate());
        result.put("endDate", workExperience.getEndDate());
        result.put("currentlyWorking", workExperience.getCurrentlyWorking());
        result.put("description", workExperience.getDescription());
        return result;
    }

    private Map<String, Object> toProjectResponse(Long cvId, ProjectEntry projectEntry) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("id", projectEntry.getId());
        result.put("cvId", cvId);
        result.put("projectName", projectEntry.getProjectName());
        result.put("role", projectEntry.getRole());
        result.put("techStack", projectEntry.getTechStack());
        result.put("startDate", projectEntry.getStartDate());
        result.put("endDate", projectEntry.getEndDate());
        result.put("projectUrl", projectEntry.getProjectUrl());
        result.put("description", projectEntry.getDescription());
        return result;
    }

    private Map<String, Object> toCertificateResponse(Long cvId, CertificateEntry certificateEntry) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("id", certificateEntry.getId());
        result.put("cvId", cvId);
        result.put("certificateName", certificateEntry.getCertificateName());
        result.put("issuer", certificateEntry.getIssuer());
        result.put("issueDate", certificateEntry.getIssueDate());
        result.put("credentialId", certificateEntry.getCredentialId());
        result.put("credentialUrl", certificateEntry.getCredentialUrl());
        return result;
    }

    private Map<String, Object> toSkillResponse(Long cvId, SkillEntry skillEntry) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("id", skillEntry.getId());
        result.put("cvId", cvId);
        result.put("skillName", skillEntry.getSkillName());
        result.put("level", skillEntry.getLevel());
        return result;
    }

    private Map<String, Object> toLanguageResponse(Long cvId, LanguageEntry languageEntry) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("id", languageEntry.getId());
        result.put("cvId", cvId);
        result.put("languageName", languageEntry.getLanguageName());
        result.put("proficiencyLevel", languageEntry.getProficiencyLevel());
        return result;
    }

        public record CreateCvRequest(
            @NotBlank(message = "title wajib diisi")
            @Size(max = 120, message = "title maksimal 120 karakter")
            String title) {
        }

        public record PersonalInformationRequest(
            @NotBlank(message = "fullName wajib diisi")
            @Size(max = 120, message = "fullName maksimal 120 karakter")
            String fullName,

            @NotBlank(message = "email wajib diisi")
            @Email(message = "format email tidak valid")
            @Size(max = 120, message = "email maksimal 120 karakter")
            String email,

            @NotBlank(message = "phoneNumber wajib diisi")
            @Pattern(regexp = "^[0-9+()\\-\\s]{8,20}$", message = "phoneNumber harus 8-20 karakter angka")
            String phoneNumber,

            @NotBlank(message = "addressLocation wajib diisi")
            @Size(max = 200, message = "addressLocation maksimal 200 karakter")
            String addressLocation,

            @NotBlank(message = "linkedInUrl wajib diisi")
            @Pattern(regexp = "^https?://.+$", message = "linkedInUrl harus URL http/https")
            @Size(max = 200, message = "linkedInUrl maksimal 200 karakter")
            String linkedInUrl,

            @Size(max = 200, message = "portfolioUrl maksimal 200 karakter")
            @Pattern(regexp = "^$|^https?://.+$", message = "portfolioUrl harus URL http/https")
            String portfolioUrl,

            @NotBlank(message = "postalCode wajib diisi")
            @Pattern(regexp = "^[A-Za-z0-9\\-\\s]{3,12}$", message = "postalCode tidak valid")
            String postalCode) {
        }

        public record SummaryRequest(
            @NotBlank(message = "content wajib diisi")
            @Size(max = 3000, message = "content maksimal 3000 karakter")
            String content) {
        }

        public record EducationRequest(
            @NotBlank(message = "institution wajib diisi")
            @Size(max = 150, message = "institution maksimal 150 karakter")
            String institution,

            @NotBlank(message = "degree wajib diisi")
            @Size(max = 100, message = "degree maksimal 100 karakter")
            String degree,

            @NotBlank(message = "fieldOfStudy wajib diisi")
            @Size(max = 100, message = "fieldOfStudy maksimal 100 karakter")
            String fieldOfStudy,

            @NotBlank(message = "startDate wajib diisi")
            @Pattern(regexp = "^(19|20)\\d{2}-(0[1-9]|1[0-2])$", message = "startDate harus format YYYY-MM")
            String startDate,

            @Pattern(regexp = "^$|^(19|20)\\d{2}-(0[1-9]|1[0-2])$", message = "endDate harus format YYYY-MM")
            String endDate,

            @Pattern(regexp = "^$|^[0-4](\\.[0-9]{1,2})?$", message = "gpa harus format 0.00 sampai 4.00")
            @Size(max = 10, message = "gpa maksimal 10 karakter")
            String gpa,

            @Size(max = 3000, message = "description maksimal 3000 karakter")
            String description) {
        }

        public record WorkExperienceRequest(
            @NotBlank(message = "company wajib diisi")
            @Size(max = 150, message = "company maksimal 150 karakter")
            String company,

            @NotBlank(message = "position wajib diisi")
            @Size(max = 120, message = "position maksimal 120 karakter")
            String position,

            @Size(max = 120, message = "location maksimal 120 karakter")
            String location,

            @NotBlank(message = "startDate wajib diisi")
            @Pattern(regexp = "^(19|20)\\d{2}-(0[1-9]|1[0-2])$", message = "startDate harus format YYYY-MM")
            String startDate,

            @Pattern(regexp = "^$|^(19|20)\\d{2}-(0[1-9]|1[0-2])$", message = "endDate harus format YYYY-MM")
            String endDate,

            Boolean currentlyWorking,

            @Size(max = 3000, message = "description maksimal 3000 karakter")
            String description) {
        }

        public record ProjectRequest(
            @NotBlank(message = "projectName wajib diisi")
            @Size(max = 160, message = "projectName maksimal 160 karakter")
            String projectName,

            @Size(max = 120, message = "role maksimal 120 karakter")
            String role,

            @Size(max = 200, message = "techStack maksimal 200 karakter")
            String techStack,

            @NotBlank(message = "startDate wajib diisi")
            @Pattern(regexp = "^(19|20)\\d{2}-(0[1-9]|1[0-2])$", message = "startDate harus format YYYY-MM")
            String startDate,

            @Pattern(regexp = "^$|^(19|20)\\d{2}-(0[1-9]|1[0-2])$", message = "endDate harus format YYYY-MM")
            String endDate,

            @Size(max = 250, message = "projectUrl maksimal 250 karakter")
            @Pattern(regexp = "^$|^https?://.+$", message = "projectUrl harus URL http/https")
            String projectUrl,

            @Size(max = 3000, message = "description maksimal 3000 karakter")
            String description) {
        }

        public record CertificateRequest(
            @NotBlank(message = "certificateName wajib diisi")
            @Size(max = 180, message = "certificateName maksimal 180 karakter")
            String certificateName,

            @NotBlank(message = "issuer wajib diisi")
            @Size(max = 160, message = "issuer maksimal 160 karakter")
            String issuer,

            @NotBlank(message = "issueDate wajib diisi")
            @Pattern(regexp = "^(19|20)\\d{2}-(0[1-9]|1[0-2])$", message = "issueDate harus format YYYY-MM")
            String issueDate,

            @Size(max = 100, message = "credentialId maksimal 100 karakter")
            String credentialId,

            @Size(max = 250, message = "credentialUrl maksimal 250 karakter")
            @Pattern(regexp = "^$|^https?://.+$", message = "credentialUrl harus URL http/https")
            String credentialUrl) {
        }

        public record SkillRequest(
            @NotBlank(message = "skillName wajib diisi")
            @Size(max = 120, message = "skillName maksimal 120 karakter")
            String skillName,

            @Size(max = 50, message = "level maksimal 50 karakter")
            String level) {
        }

        public record LanguageRequest(
            @NotBlank(message = "languageName wajib diisi")
            @Size(max = 80, message = "languageName maksimal 80 karakter")
            String languageName,

            @NotBlank(message = "proficiencyLevel wajib diisi")
            @Size(max = 50, message = "proficiencyLevel maksimal 50 karakter")
            String proficiencyLevel) {
        }
}
