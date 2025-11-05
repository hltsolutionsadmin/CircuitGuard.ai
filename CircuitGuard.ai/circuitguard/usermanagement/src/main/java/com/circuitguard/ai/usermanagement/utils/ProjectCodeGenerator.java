package com.circuitguard.ai.usermanagement.utils;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Component
public class ProjectCodeGenerator {

    private static final SecureRandom RANDOM = new SecureRandom();
    // Compact date to keep code length under 10: yyMM (4 chars)
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyMM");
    private static final int RANDOM_SUFFIX_LENGTH = 2; // 2 hex chars
    private static final int ABBR_LENGTH = 3;          // 3 chars from project name


    public String generateCode(String projectName) {
        // Result format (no separators):
        //   <ABBR(3)><DATE(yyMM:4)><HEX(2)>  => total length = 9 (< 10)
        String abbreviation = extractAbbreviation(projectName);
        String datePart = LocalDate.now().format(DATE_FORMAT);
        String randomPart = randomHex(RANDOM_SUFFIX_LENGTH);

        return abbreviation + datePart + randomPart;
    }

    private String extractAbbreviation(String name) {
        if (name == null || name.isEmpty()) return "GEN";
        StringBuilder sb = new StringBuilder(ABBR_LENGTH);
        for (int i = 0; i < name.length() && sb.length() < ABBR_LENGTH; i++) {
            char c = name.charAt(i);
            if (Character.isLetterOrDigit(c)) {
                sb.append(Character.toUpperCase(c));
            }
        }
        // If not enough alnum chars, pad with 'X'
        while (sb.length() < ABBR_LENGTH) sb.append('X');
        return sb.toString();
    }

    /**
     * Generates a random hexadecimal string (A–F, 0–9) of given length.
     */
    private String randomHex(int length) {
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(Integer.toHexString(RANDOM.nextInt(16)).toUpperCase());
        }
        return sb.toString();
    }
}
