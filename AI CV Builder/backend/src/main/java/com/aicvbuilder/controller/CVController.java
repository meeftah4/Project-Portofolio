package com.aicvbuilder.controller;

import com.aicvbuilder.dto.CVRequest;
import com.aicvbuilder.entity.CV;
import com.aicvbuilder.entity.User;
import com.aicvbuilder.service.CVService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cv")
@RequiredArgsConstructor
@Tag(name = "CV Management", description = "CV CRUD operations")
public class CVController {

    private final CVService cvService;

    @GetMapping
    @Operation(summary = "Get all user's CVs")
    public ResponseEntity<List<CV>> getUserCVs(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        List<CV> cvs = cvService.getUserCVs(user);
        return ResponseEntity.ok(cvs);
    }

    @PostMapping
    @Operation(summary = "Create new CV")
    public ResponseEntity<CV> createCV(@RequestBody CVRequest request, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        CV cv = cvService.createCV(user, request.getTitle(), request.getDescription(), request.getContent());
        return ResponseEntity.status(HttpStatus.CREATED).body(cv);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get CV by ID")
    public ResponseEntity<CV> getCVById(@PathVariable Long id, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        CV cv = cvService.getCVById(id, user);
        return ResponseEntity.ok(cv);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update CV")
    public ResponseEntity<CV> updateCV(@PathVariable Long id, @RequestBody CVRequest request, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        CV cv = cvService.updateCV(id, user, request.getTitle(), request.getDescription(), request.getContent());
        return ResponseEntity.ok(cv);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete CV")
    public ResponseEntity<Void> deleteCV(@PathVariable Long id, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        cvService.deleteCV(id, user);
        return ResponseEntity.noContent().build();
    }
}
