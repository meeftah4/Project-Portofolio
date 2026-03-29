package com.belajar.belajarspringboot;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.belajar.belajarspringboot.model.UserAccount;
import com.belajar.belajarspringboot.model.UserRole;
import com.belajar.belajarspringboot.repository.UserAccountRepository;
import com.belajar.belajarspringboot.security.JwtService;
import com.belajar.belajarspringboot.security.RefreshTokenService;
import com.belajar.belajarspringboot.security.TokenBlacklistService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserAccountRepository userAccountRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final TokenBlacklistService tokenBlacklistService;

    public AuthController(
            UserAccountRepository userAccountRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService,
            RefreshTokenService refreshTokenService,
            TokenBlacklistService tokenBlacklistService) {
        this.userAccountRepository = userAccountRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
        this.tokenBlacklistService = tokenBlacklistService;
    }

    @PostMapping("/register")
    public Map<String, Object> register(@Valid @RequestBody RegisterRequest request) {
        String username = request.username().trim();
        String email = request.email().trim().toLowerCase();

        if (userAccountRepository.existsByUsernameIgnoreCase(username)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Username sudah dipakai");
        }
        if (userAccountRepository.existsByEmailIgnoreCase(email)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email sudah dipakai");
        }

        UserAccount account = new UserAccount();
        account.setUsername(username);
        account.setEmail(email);
        account.setPasswordHash(passwordEncoder.encode(request.password()));
        account.setFullName(trimToNull(request.fullName()));
        account.setRole(UserRole.USER);

        UserAccount saved = userAccountRepository.save(account);
        String accessToken = jwtService.generateToken(saved);
        String refreshToken = refreshTokenService.issueRefreshToken(saved);

        return toAuthResponse(saved, accessToken, refreshToken);
    }

    @PostMapping("/login")
    public Map<String, Object> login(@Valid @RequestBody LoginRequest request) {
        String email = request.email().trim().toLowerCase();

        UserAccount account = userAccountRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email atau password salah"));

        if (!passwordEncoder.matches(request.password(), account.getPasswordHash())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Email atau password salah");
        }

        String accessToken = jwtService.generateToken(account);
        String refreshToken = refreshTokenService.issueRefreshToken(account);

        return toAuthResponse(account, accessToken, refreshToken);
    }

    @PostMapping("/refresh")
    public Map<String, Object> refresh(@Valid @RequestBody RefreshRequest request) {
        RefreshTokenService.RotationResult rotationResult = refreshTokenService
                .rotateRefreshToken(request.refreshToken().trim());

        String accessToken = jwtService.generateToken(rotationResult.userAccount());
        return toAuthResponse(rotationResult.userAccount(), accessToken, rotationResult.refreshToken());
    }

    @PostMapping("/logout")
    public Map<String, Object> logout(
            @RequestBody(required = false) LogoutRequest request,
            HttpServletRequest httpServletRequest) {

        String accessToken = extractBearerToken(httpServletRequest.getHeader("Authorization"));
        if (accessToken != null) {
            tokenBlacklistService.blacklistAccessToken(accessToken);
        }

        if (request != null) {
            refreshTokenService.revokeRefreshToken(request.refreshToken());
        }

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("message", "Logout berhasil");
        return result;
    }

    @GetMapping("/me")
    public Map<String, Object> me(Authentication authentication) {
        UserAccount account = getCurrentAccount(authentication);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("user", toUserResponse(account));
        return result;
    }

    private UserAccount getCurrentAccount(Authentication authentication) {
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

    private Map<String, Object> toAuthResponse(UserAccount account, String accessToken, String refreshToken) {
        Map<String, Object> result = new LinkedHashMap<>();
        result.put("token", accessToken);
        result.put("accessToken", accessToken);
        result.put("refreshToken", refreshToken);
        result.put("tokenType", "Bearer");
        result.put("accessTokenExpiresInHours", jwtService.getExpirationHours());
        result.put("user", toUserResponse(account));
        return result;
    }

    private String extractBearerToken(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }

        String token = authHeader.substring(7).trim();
        return token.isEmpty() ? null : token;
    }

    private Map<String, Object> toUserResponse(UserAccount account) {
        Map<String, Object> user = new LinkedHashMap<>();
        user.put("id", account.getId());
        user.put("username", account.getUsername());
        user.put("email", account.getEmail());
        user.put("fullName", account.getFullName());
        user.put("role", account.getRole().name());
        user.put("createdAt", account.getCreatedAt());
        return user;
    }

    private String trimToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }

    public record RegisterRequest(
            @NotBlank(message = "username wajib diisi")
            @Size(min = 3, max = 50, message = "username harus 3-50 karakter")
            @Pattern(regexp = "^[A-Za-z0-9_.-]+$", message = "username hanya boleh huruf, angka, _, ., -")
            String username,

            @NotBlank(message = "email wajib diisi")
            @Email(message = "format email tidak valid")
            @Size(max = 120, message = "email maksimal 120 karakter")
            String email,

            @NotBlank(message = "password wajib diisi")
            @Size(min = 8, max = 100, message = "password harus 8-100 karakter")
            String password,

            @Size(max = 120, message = "fullName maksimal 120 karakter")
            String fullName) {
    }

    public record LoginRequest(
            @NotBlank(message = "email wajib diisi")
            @Email(message = "format email tidak valid")
            @Size(max = 120, message = "email maksimal 120 karakter")
            String email,

            @NotBlank(message = "password wajib diisi")
            @Size(max = 100, message = "password maksimal 100 karakter")
            String password) {
    }

            public record RefreshRequest(
                @NotBlank(message = "refreshToken wajib diisi")
                String refreshToken) {
            }

            public record LogoutRequest(String refreshToken) {
            }
}
