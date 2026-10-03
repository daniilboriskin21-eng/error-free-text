package com.example.errorfreetext.correction;

import java.util.ArrayList;
import java.util.List;

public class TextSplitter {

    public List<String> split(String text, int maxLength) {
        if (text == null) throw new IllegalArgumentException("Text must not be null");
        if (maxLength <= 0) throw new IllegalArgumentException("Max length must be positive");
        if (text.isEmpty()) return List.of();

        List<String> parts = new ArrayList<>();
        int start = 0;

        while (start < text.length()) {
            int chunkLength = Math.min(maxLength, text.length() - start);
            int end = start + chunkLength;

            if (end < text.length()) {
                for(int i = end - 1; i >= start; i--) {
                    if (Character.isWhitespace(text.charAt(i))) {
                        end = i + 1;
                        break;
                    }
                }
            }

            if (end < text.length()
                    && Character.isHighSurrogate(text.charAt(end - 1))
                    && Character.isLowSurrogate(text.charAt(end))) {
                end--;
            }

            if (end == start) {
                throw new IllegalArgumentException(
                        "Max length is too small to keep a Unicode character intact"
                );
            }

            parts.add(text.substring(start, end));
            start = end;
        }

        return parts;
    }
}
