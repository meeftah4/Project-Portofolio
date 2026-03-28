package com.aicvbuilder.service;

import com.azure.ai.openai.OpenAIClient;
import com.azure.ai.openai.OpenAIClientBuilder;
import com.azure.core.credential.AzureKeyCredential;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AzureAIService {

    @Value("${azure.openai.api-key}")
    private String apiKey;

    @Value("${azure.openai.endpoint}")
    private String endpoint;

    @Value("${azure.openai.deployment-name}")
    private String deploymentName;

    private OpenAIClient openAIClient;

    private OpenAIClient getClient() {
        if (openAIClient == null) {
            openAIClient = new OpenAIClientBuilder()
                    .endpoint(endpoint)
                    .credential(new AzureKeyCredential(apiKey))
                    .buildClient();
        }
        return openAIClient;
    }

    public String reviewCV(String cvContent) {
        // TODO: Implement CV review using Azure OpenAI
        return "CV review will be implemented with Azure OpenAI API";
    }

    public Double calculateATSScore(String cvContent) {
        // TODO: Implement ATS score calculation
        return 0.0;
    }

    public String getKeywordSuggestions(String cvContent, String jobDescription) {
        // TODO: Implement keyword suggestions
        return "Keyword suggestions will be generated";
    }

    public String checkGrammar(String text) {
        // TODO: Implement grammar check
        return "Grammar check results";
    }
}
