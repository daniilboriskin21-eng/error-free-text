package com.example.errorfreetext.correction;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TextSplitterTest {

    @Test
    void shouldSplitAtWhitespace() {
        var splitter = new TextSplitter();

        var parts = splitter.split("Hello world!", 8);

        assertEquals(List.of("Hello ", "world!"), parts);
        assertEquals("Hello world!", String.join("", parts));
    }

    @Test
    void shouldSplitLongWord() {
        var splitter = new TextSplitter();

        var parts = splitter.split("abcdefghij", 4);

        assertEquals(List.of("abcd", "efgh", "ij"), parts);
        assertEquals("abcdefghij", String.join("", parts));
    }

    @Test
    void shouldKeepTextAtExactLimit() {
        var splitter = new TextSplitter();

        var parts = splitter.split("Hi world", 8);

        assertEquals(List.of("Hi world"), parts);
        assertEquals("Hi world", String.join("", parts));
    }

    @Test
    void shouldPreserveLineBreaks() {
        var splitter = new TextSplitter();

        var parts = splitter.split("abc\ndef\nghi", 5);

        assertEquals(List.of("abc\n", "def\n", "ghi"), parts);
        assertEquals("abc\ndef\nghi", String.join("", parts));
    }

    @Test
    void shouldReturnEmptyListForEmptyText() {
        var splitter = new TextSplitter();

        var parts = splitter.split("", 10);

        assertEquals(List.of(), parts);
        assertEquals("", String.join("", parts));
    }

    @Test
    void shouldRejectZeroLimit() {
        var splitter = new TextSplitter();

        assertThrows(
                IllegalArgumentException.class,
                () -> splitter.split("Hello", 0)
        );
    }

    @Test
    void shouldRejectNegativeLimit() {
        var splitter = new TextSplitter();

        assertThrows(
                IllegalArgumentException.class,
                () -> splitter.split("Hello", -1)
        );
    }

    @Test
    void shouldRejectNullText() {
        var splitter = new TextSplitter();

        assertThrows(
                IllegalArgumentException.class,
                () -> splitter.split(null, 10)
        );
    }

    @Test
    void shouldNotSplitSurrogatePair() {
        var splitter = new TextSplitter();

        var parts = splitter.split("ab😀cd", 3);

        assertEquals(List.of("ab", "😀c", "d"), parts);
        assertEquals("ab😀cd", String.join("", parts));
    }

    @Test
    void shouldRejectLimitTooSmallForSurrogatePair() {
        var splitter = new TextSplitter();

        assertThrows(
                IllegalArgumentException.class,
                () -> splitter.split("😀", 1)
        );
    }
}