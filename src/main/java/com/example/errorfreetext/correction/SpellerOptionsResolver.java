package com.example.errorfreetext.correction;

import java.util.regex.Pattern;

public class SpellerOptionsResolver {
    private static final int IGNORE_DIGITS = 2;
    private static final int IGNORE_URLS = 4;

    private static final Pattern URL_PATTERN = Pattern.compile(
            "\\b(?:https?://|www\\.)[^\\s<>]+",
            Pattern.CASE_INSENSITIVE
    );

    public int resolve(String text) {
        if (text == null) throw new IllegalArgumentException("Text must not be null");

        int options = 0;
        if (text.codePoints().anyMatch(Character::isDigit)) {
            options |= IGNORE_DIGITS;
        }

        if (URL_PATTERN.matcher(text).find()) {
            options |= IGNORE_URLS;
        }

        return options;
    }
}
