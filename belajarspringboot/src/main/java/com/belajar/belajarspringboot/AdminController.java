package com.belajar.belajarspringboot;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.belajar.belajarspringboot.model.UserAccount;
import com.belajar.belajarspringboot.model.UserRole;
import com.belajar.belajarspringboot.repository.CvRepository;
import com.belajar.belajarspringboot.repository.UserAccountRepository;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final UserAccountRepository userAccountRepository;
    private final CvRepository cvRepository;

    public AdminController(UserAccountRepository userAccountRepository, CvRepository cvRepository) {
        this.userAccountRepository = userAccountRepository;
        this.cvRepository = cvRepository;
    }

    @GetMapping("/dashboard")
    public Map<String, Object> dashboard() {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("generatedAt", Instant.now().toString());
        result.put("totalUsers", userAccountRepository.count());
        result.put("totalAdmins", userAccountRepository.countByRole(UserRole.ADMIN));
        result.put("totalRegularUsers", userAccountRepository.countByRole(UserRole.USER));
        result.put("totalCvs", cvRepository.count());
        result.put("totalCvsWithoutOwner", cvRepository.countByOwnerIsNull());

        return result;
    }

    @GetMapping("/users")
    public List<Map<String, Object>> users() {
        return userAccountRepository.findAllByOrderByIdAsc().stream()
                .map(this::toUserSummary)
                .toList();
    }

    @PostMapping("/users/{userId}/promote")
    public Map<String, Object> promoteToAdmin(@PathVariable Long userId) {
        UserAccount account = userAccountRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User tidak ditemukan"));

        String message;
        if (account.getRole() == UserRole.ADMIN) {
            message = "User sudah menjadi ADMIN";
        } else {
            account.setRole(UserRole.ADMIN);
            userAccountRepository.save(account);
            message = "User berhasil dipromosikan menjadi ADMIN";
        }

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("message", message);
        result.put("user", toUserSummary(account));
        return result;
    }

    private Map<String, Object> toUserSummary(UserAccount account) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("id", account.getId());
        result.put("username", account.getUsername());
        result.put("email", account.getEmail());
        result.put("fullName", account.getFullName());
        result.put("role", account.getRole().name());
        result.put("createdAt", account.getCreatedAt());
        result.put("totalCvs", cvRepository.countByOwnerId(account.getId()));
        return result;
    }
}
