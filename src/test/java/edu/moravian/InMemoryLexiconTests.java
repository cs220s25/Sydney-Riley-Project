package edu.moravian;

import edu.moravian.exceptions.*;
import org.junit.jupiter.api.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

public class InMemoryLexiconTests {

    @Test
    public void testAlphabetize() {
        InMemoryLexicon lexicon = new InMemoryLexicon();
        assertEquals("ehors", lexicon.alphabetize("hrose"));
        assertEquals("ehors", lexicon.alphabetize("esohr"));
        assertEquals("ehors", lexicon.alphabetize("orhes"));
    }

    @Test
    public void testGetAlphabetize() throws StorageException {
        InMemoryLexicon lexicon = new InMemoryLexicon();
        assertEquals("ehors", lexicon.getAlphabetizeWord("hrose"));
        assertEquals("ehors", lexicon.getAlphabetizeWord("esohr"));
        assertEquals("ehors", lexicon.getAlphabetizeWord("orhes"));
    }

    @Test
    public void testBuildLexicon() throws StorageException {
        InMemoryLexicon lexicon = new InMemoryLexicon();
        List<String> words = Arrays.asList("horse", "hrose", "esohr", "paorrt", "aorrpt");
        lexicon.buildLexicon(words);

        assertEquals(Arrays.asList("horse", "hrose", "esohr"), lexicon.getWordsWithSameLetters("horse"));
        assertEquals(Arrays.asList("paorrt", "aorrpt"), lexicon.getWordsWithSameLetters("parrot"));
        assertEquals(Arrays.asList("paorrt", "aorrpt"), lexicon.getWordsWithSameLetters("raptor"));
    }

    @Test
    public void testGetWordsWithSameLetters() throws StorageException {
        InMemoryLexicon lexicon = new InMemoryLexicon();
        List<String> words = Arrays.asList("horse", "hrose", "esohr", "paorrt", "aorrpt", "lalam");
        lexicon.buildLexicon(words);

        assertEquals(Arrays.asList("horse", "hrose", "esohr"), lexicon.getWordsWithSameLetters("horse"));
        assertEquals(Arrays.asList("paorrt", "aorrpt"), lexicon.getWordsWithSameLetters("parrot"));
        assertEquals(Arrays.asList("paorrt", "aorrpt"), lexicon.getWordsWithSameLetters("raptor"));
        assertEquals(List.of("lalam"), lexicon.getWordsWithSameLetters("llama"));
    }
}