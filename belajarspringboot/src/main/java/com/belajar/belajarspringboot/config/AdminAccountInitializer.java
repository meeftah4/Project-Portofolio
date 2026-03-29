package com.belajar.belajarspringboot.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.belajar.belajarspringboot.model.UserAccount;
import com.belajar.belajarspringboot.model.UserRole;
import com.belajar.belajarspringboot.repository.UserAccountRepository;

@Component
public class AdminAccountInitializer implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminAccountInitializer.class);

    private final UserAccountRepository userAccountRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.admin.bootstrap-enabled:true}")
    private boolean bootstrapEnabled;

    @Value("${app.admin.username:admin}")
    private String adminUsername;

    @Value("${app.admin.email:admin@local.dev}")
    private String adminEmail;

    @Value("${app.admin.password:Admin12345}")
    private String adminPassword;

    @Value("${app.admin.full-name:System Administrator}")
    private String adminFullName;

    public AdminAccountInitializer(UserAccountRepository userAccountRepository, PasswordEncoder passwordEncoder) {
        this.userAccountRepository = userAccountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (!bootstrapEnabled) {
            return;
        }

        if (userAccountRepository.existsByEmailIgnoreCase(adminEmail)) {
            return;
        }

        UserAccount admin = new UserAccount();
        admin.setUsername(adminUsername.trim());
        admin.setEmail(adminEmail.trim().toLowerCase());
        admin.setPasswordHash(passwordEncoder.encode(adminPassword));
        admin.setFullName(adminFullName.trim());
        admin.setRole(UserRole.ADMIN);

        userAccountRepository.save(admin);

        LOGGER.warn("Admin default dibuat: email='{}' username='{}'. Ubah password default secepatnya.", adminEmail,
                adminUsername);
    }
}
