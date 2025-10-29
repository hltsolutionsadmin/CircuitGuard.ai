package com.circuitguard.ai.usermanagement.utils;

import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class ProjectCodeGenerator {

    private final AtomicInteger sequence = new AtomicInteger(1);
    private static final DecimalFormat FORMATTER = new DecimalFormat("000");

    public String generateCode(String projectName) {
        String prefix = extractPrefix(projectName);
        String number = FORMATTER.format(sequence.getAndIncrement());
        return prefix + "-" + number;
    }

    private String extractPrefix(String name) {
        if (name == null || name.isEmpty()) return "PRJ";
        String[] words = name.split(" ");
        StringBuilder sb = new StringBuilder();
        for (String w : words) {
            if (!w.isEmpty()) sb.append(Character.toUpperCase(w.charAt(0)));
            if (sb.length() >= 4) break; // Limit prefix length
        }
        return sb.toString();
    }
}
