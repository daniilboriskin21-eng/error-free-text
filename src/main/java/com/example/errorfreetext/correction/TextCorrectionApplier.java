package com.example.errorfreetext.correction;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TextCorrectionApplier {
    public String apply(String text, List<TextReplacement> replacements) {
        if (text == null) throw new IllegalArgumentException("Text must not be null");
        if (replacements == null) throw new IllegalArgumentException("Replacements must not be null");
        if (replacements.isEmpty()) return text;

        for (TextReplacement replacement : replacements) {
            // Проверки одной замены
            if (replacement == null) throw new IllegalArgumentException("Replacement must not be null");
            if (replacement.replacement() == null) throw new IllegalArgumentException("Replacement text must not be null");
            if (replacement.position() < 0
                    || replacement.length() <= 0
                    || replacement.position() > text.length()
                    || replacement.length() > text.length() - replacement.position()) {
                throw new IllegalArgumentException("Replacement range is invalid");
            }
        }

        List<TextReplacement> sorted = new ArrayList<>(replacements);
        sorted.sort(Comparator.comparingInt(TextReplacement::position).reversed());

        int rightBoundary = text.length();

        for (TextReplacement replacement : sorted) {
            int end = replacement.position() + replacement.length();

            if (end > rightBoundary) {
                throw new IllegalArgumentException("Replacement ranges overlap");
            }

            rightBoundary = replacement.position();
        }

        StringBuilder result = new StringBuilder(text);

        for (TextReplacement replacement : sorted) {
            result.replace(
                    replacement.position(),
                    replacement.position() + replacement.length(),
                    replacement.replacement()
            );
        }

        return result.toString();
    }
}
