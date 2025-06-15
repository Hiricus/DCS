package com.hiricus.dcs.service.template.replacing;


public abstract class ReplacingStrategy {
    private final ReplacementKeyword keyword;

    public ReplacingStrategy(String keyword) {
        this.keyword = ReplacementKeyword.valueOf(keyword);
    }

    public abstract String getReplacement(Integer studentId);

    public ReplacementKeyword getKeyword() {
        return keyword;
    }
}
