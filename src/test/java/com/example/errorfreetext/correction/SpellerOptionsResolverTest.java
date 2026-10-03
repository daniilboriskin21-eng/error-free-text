package com.example.errorfreetext.correction;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SpellerOptionsResolverTest {
    @Test
    void shouldReturnZeroForPlainText() {
        var resolver = new SpellerOptionsResolver();

        int options = resolver.resolve("Hello world");

        assertEquals(0, options);
    }

    @Test
    void shouldIgnoreDigits() {
        var resolver = new SpellerOptionsResolver();

        int options = resolver.resolve("Hello 123");

        assertEquals(2, options);
    }

    @Test
    void shouldIgnoreHttpsUrl() {
        var resolver = new SpellerOptionsResolver();

        int options = resolver.resolve("Visit https://example.com");

        assertEquals(4, options);
    }

    @Test
    void shouldCombineBothOptions() {
        var resolver = new SpellerOptionsResolver();

        int options = resolver.resolve("Visit https://example.com/page2");

        assertEquals(6, options);
    }

    @Test
    void shouldRecognizeUppercaseHttpUrl() {
        var resolver = new SpellerOptionsResolver();

        int options = resolver.resolve("Visit HTTP://EXAMPLE.COM");

        assertEquals(4, options);
    }

    @Test
    void shouldRecognizeWwwUrl() {
        var resolver = new SpellerOptionsResolver();

        int options = resolver.resolve("Visit www.example.com");

        assertEquals(4, options);
    }

    @Test
    void shouldReturnZeroForEmptyText() {
        var resolver = new SpellerOptionsResolver();

        int options = resolver.resolve("");

        assertEquals(0, options);
    }

    @Test
    void shouldRejectNullText() {
        var resolver = new SpellerOptionsResolver();

        assertThrows(
                IllegalArgumentException.class,
                () -> resolver.resolve(null)
        );
    }
}
