package com.aicvbuilder.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CVRequest {
    private String title;
    private String description;
    private String content; // JSON format
}
