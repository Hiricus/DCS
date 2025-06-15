package com.hiricus.dcs.service.template;

import com.hiricus.dcs.service.template.replacing.ReplacingStrategy;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class KeywordReplacer {
    private final List<ReplacingStrategy> strategies;
    private Map<String, ReplacingStrategy> mappedStrategies;

    @Autowired
    public KeywordReplacer(List<ReplacingStrategy> strategies) {
        this.strategies = strategies;
    }

    @PostConstruct
    private void init() {
        mappedStrategies = new HashMap<>();
        for (ReplacingStrategy strategy : strategies) {
            mappedStrategies.put(strategy.getKeyword().toString(), strategy);
        }

//        System.out.println("Keywords for replacement:");
//        for (String string : mappedStrategies.keySet()) {
//            System.out.println(string);
//        }
    }

    public String replaceKeyword(String keywordString, Integer studentId) {
        if (!mappedStrategies.containsKey(keywordString)) {
            throw new RuntimeException("Keyword does not match any known keywords: " + keywordString);
        }

        ReplacingStrategy replacingStrategy = mappedStrategies.get(keywordString);
        return replacingStrategy.getReplacement(studentId);
    }
}
