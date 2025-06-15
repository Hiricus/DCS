package com.hiricus.dcs.service.template.replacing;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class CurrentDateReplacingStrategy extends ReplacingStrategy {
    public CurrentDateReplacingStrategy(@Value("CURRENT_DATE") String keyword) {
        super(keyword);
    }

    @Override
    public String getReplacement(Integer studentId) {
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        return today.format(formatter);
    }
}
