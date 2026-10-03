package com.example.errorfreetext.correction;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TextCorrectionApplierTest {
    @Test
    void shouldApplyReplacementsWithoutShiftingPositions() {
        var applier = new TextCorrectionApplier();
        var replacements = List.of(
                new TextReplacement(0, 4, "Hello"),
                new TextReplacement(5, 4, "world")
        );

        String result = applier.apply("Helo wrld", replacements);

        assertEquals("Hello world", result);
    }

    @Test
    void shouldReturnOriginalTextWhenNoReplacements() {
        var applier = new TextCorrectionApplier();
        List<TextReplacement> replacements = List.of();

        String result = applier.apply("Hello\nworld", replacements);

        assertEquals("Hello\nworld", result);
    }

    @Test
    void shouldReturnEmptyTextWhenNoReplacements() {
        var applier = new TextCorrectionApplier();
        List<TextReplacement> replacements = List.of();

        String result = applier.apply("", replacements);

        assertEquals("", result);
    }

    @Test
    void shouldReplaceWholeText() {
        var applier = new TextCorrectionApplier();
        var replacements = List.of(
                new TextReplacement(0, 4, "Hello")
        );

        String result = applier.apply("Helo", replacements);

        assertEquals("Hello", result);
    }

    @Test
    void shouldApplyShorterReplacement() {
        var applier = new TextCorrectionApplier();
        var replacements = List.of(
                new TextReplacement(0, 6, "Hello")
        );

        String result = applier.apply("Helllo world", replacements);

        assertEquals("Hello world", result);
    }

    @Test
    void shouldDeleteText() {
        var applier = new TextCorrectionApplier();
        var replacements = List.of(
                new TextReplacement(6, 6, "")
        );

        String result = applier.apply("Hello extra world", replacements);

        assertEquals("Hello world", result);
    }

    @Test
    void shouldAllowAdjacentReplacements() {
        var applier = new TextCorrectionApplier();
        var replacements = List.of(
                new TextReplacement(0, 2, "X"),
                new TextReplacement(2, 2, "Y")
        );

        String result = applier.apply("abcd", replacements);

        assertEquals("XY", result);
    }

    @Test
    void shouldPreserveWhitespaceOutsideReplacements() {
        var applier = new TextCorrectionApplier();
        var replacements = List.of(
                new TextReplacement(2, 4, "Hello"),
                new TextReplacement(7, 4, "world")
        );

        String result = applier.apply("  Helo\nwrld!  ", replacements);

        assertEquals("  Hello\nworld!  ", result);
    }

    @Test
    void shouldNotModifyOriginalList() {
        var applier = new TextCorrectionApplier();
        var replacements = new ArrayList<>(List.of(
                new TextReplacement(0, 4, "Hello"),
                new TextReplacement(5, 4, "world")
        ));
        var originalOrder = List.copyOf(replacements);

        String result = applier.apply("Helo wrld", replacements);

        assertEquals("Hello world", result);

        assertEquals(originalOrder, replacements);
    }

    @Test
    void shouldRejectNullText() {
        var applier = new TextCorrectionApplier();

        assertThrows(
                IllegalArgumentException.class,
                () -> applier.apply(null, List.of())
        );
    }

    @Test
    void shouldRejectNullReplacementList() {
        var applier = new TextCorrectionApplier();

        assertThrows(
                IllegalArgumentException.class,
                () -> applier.apply("Hello", null)
        );
    }

    @Test
    void shouldRejectNullReplacementElement() {
        var applier = new TextCorrectionApplier();
        List<TextReplacement> replacements = new ArrayList<>();
        replacements.add(null);

        assertThrows(
                IllegalArgumentException.class,
                () -> applier.apply("Hello", replacements)
        );
    }

    @Test
    void shouldRejectNullReplacementText() {
        var applier = new TextCorrectionApplier();
        List<TextReplacement> replacements = List.of(
                new TextReplacement(0, 1, null)
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> applier.apply("Hello", replacements)
        );
    }

    @Test
    void shouldRejectNegativePosition() {
        var applier = new TextCorrectionApplier();
        var replacements = List.of(
                new TextReplacement(-1, 1, "X")
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> applier.apply("Hello", replacements)
        );
    }

    @Test
    void shouldRejectZeroLength() {
        var applier = new TextCorrectionApplier();
        var replacements = List.of(
                new TextReplacement(0, 0, "X")
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> applier.apply("Hello", replacements)
        );
    }

    @Test
    void shouldRejectNegativeLength() {
        var applier = new TextCorrectionApplier();
        var replacements = List.of(
                new TextReplacement(0, -1, "X")
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> applier.apply("Hello", replacements)
        );
    }

    @Test
    void shouldRejectPositionBeyondText() {
        var applier = new TextCorrectionApplier();
        var replacements = List.of(
                new TextReplacement(6, 1, "X")
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> applier.apply("Hello", replacements)
        );
    }

    @Test
    void shouldRejectRangeBeyondText() {
        var applier = new TextCorrectionApplier();
        var replacements = List.of(
                new TextReplacement(4, 2, "X")
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> applier.apply("Hello", replacements)
        );
    }

    @Test
    void shouldRejectExcessivelyLargeLength() {
        var applier = new TextCorrectionApplier();
        var replacements = List.of(
                new TextReplacement(1, Integer.MAX_VALUE, "X")
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> applier.apply("Hello", replacements)
        );
    }

    @Test
    void shouldRejectOverlappingRanges() {
        var applier = new TextCorrectionApplier();
        var replacements = List.of(
                new TextReplacement(0, 3, "X"),
                new TextReplacement(2, 2, "Y")
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> applier.apply("abcdef", replacements)
        );
    }

    @Test
    void shouldRejectNestedRanges() {
        var applier = new TextCorrectionApplier();
        var replacements = List.of(
                new TextReplacement(0, 6, "X"),
                new TextReplacement(2, 2, "Y")
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> applier.apply("abcdef", replacements)
        );
    }

    @Test
    void shouldRejectDuplicateRanges() {
        var applier = new TextCorrectionApplier();
        var replacements = List.of(
                new TextReplacement(1, 2, "X"),
                new TextReplacement(1, 2, "Y")
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> applier.apply("abcdef", replacements)
        );
    }
}
