package com.aicvbuilder.controller;

import com.aicvbuilder.entity.CV;
import com.aicvbuilder.entity.User;
import com.aicvbuilder.service.AzureAIService;
import com.aicvbuilder.service.CVService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cv/{id}/ai")
@RequiredArgsConstructor
@Tag(name = "AI Analysis", description = "AI-powered CV analysis endpoints")
public class AIAnalysisController {

    private final AzureAIService aiService;
    private final CVService cvService;

    @PostMapping("/review")
    @Operation(summary = "Get AI review for CV")
    public ResponseEntity<String> reviewCV(@PathVariable Long id, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        CV cv = cvService.getCVById(id, user);
        
        String review = aiService.reviewCV(cv.getContent());
        cvService.saveAiReview(id, user, review);
        
        return ResponseEntity.ok(review);
    }

    @PostMapping("/ats-score")
    @Operation(summary = "Calculate ATS score")
    public ResponseEntity<Double> calculateATSScore(@PathVariable Long id, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        CV cv = cvService.getCVById(id, user);
        
        Double score = aiService.calculateATSScore(cv.getContent());
        cvService.updateAtsScore(id, user, score);
        
        return ResponseEntity.ok(score);
    }

    @PostMapping("/keywords")
    @Operation(summary = "Get keyword suggestions")
    public ResponseEntity<String> getKeywordSuggestions(@PathVariable Long id, 
                                                       @RequestParam(required = false) String jobDescription,
                                                       Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        CV cv = cvService.getCVById(id, user);
        
        String keywords = aiService.getKeywordSuggestions(cv.getContent(), jobDescription);
        
        return ResponseEntity.ok(keywords);
    }

    @PostMapping("/grammar")
    @Operation(summary = "Check grammar")
    public ResponseEntity<String> checkGrammar(@PathVariable Long id, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        CV cv = cvService.getCVById(id, user);
        
        String result = aiService.checkGrammar(cv.getContent());
        
        return ResponseEntity.ok(result);
    }
}
