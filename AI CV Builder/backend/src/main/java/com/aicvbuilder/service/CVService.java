package com.aicvbuilder.service;

import com.aicvbuilder.entity.CV;
import com.aicvbuilder.entity.User;
import com.aicvbuilder.repository.CVRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CVService {

    private final CVRepository cvRepository;

    public CV createCV(User user, String title, String description, String content) {
        CV cv = CV.builder()
                .user(user)
                .title(title)
                .description(description)
                .content(content)
                .build();
        return cvRepository.save(cv);
    }

    public List<CV> getUserCVs(User user) {
        return cvRepository.findByUser(user);
    }

    public CV getCVById(Long id, User user) {
        return cvRepository.findByIdAndUser(id, user)
                .orElseThrow(() -> new RuntimeException("CV not found"));
    }

    public CV updateCV(Long id, User user, String title, String description, String content) {
        CV cv = getCVById(id, user);
        cv.setTitle(title);
        cv.setDescription(description);
        cv.setContent(content);
        return cvRepository.save(cv);
    }

    public void deleteCV(Long id, User user) {
        CV cv = getCVById(id, user);
        cvRepository.delete(cv);
    }

    public CV updateAtsScore(Long id, User user, Double score) {
        CV cv = getCVById(id, user);
        cv.setAtsScore(score);
        return cvRepository.save(cv);
    }

    public CV saveAiReview(Long id, User user, String review) {
        CV cv = getCVById(id, user);
        cv.setAiReview(review);
        return cvRepository.save(cv);
    }
}
