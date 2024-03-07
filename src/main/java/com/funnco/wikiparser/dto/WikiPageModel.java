package com.funnco.wikiparser.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class WikiPageModel {
    private String url;
    private String title;
    private LocalDateTime lastTimeRedacted;
    private int wordCount;
    private String matchedText;
}
