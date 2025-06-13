package com.hiricus.dcs.util.documents;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MappingParser {
    public static Map<String, String> parseToMap(String input) {
        Map<String, String> map = new HashMap<>();

        if (input == null || input.isEmpty()) return map;

        String[] parts = input.split(";");

//        System.out.println(Arrays.toString(parts));
        if (parts.length % 2 != 0) {
            throw new IllegalArgumentException("Строка должна содержать чётное количество элементов (ключ-значение попарно).");
        }

        for (int i = 0; i < parts.length; i += 2) {
            String key = parts[i].trim();
            String value = parts[i + 1].trim();
            map.put("{" + key + "}", value);
        }

        return map;
    }
}
