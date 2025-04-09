package edu.moravian;

import edu.moravian.exceptions.*;
import java.util.*;

public class InMemoryLexicon implements Lexicon {
    private final Map<String, List<String>> lexicon;

    public InMemoryLexicon() {
        this.lexicon = new HashMap<>();
    }

    public void buildLexicon(List<String> words) throws StorageException {
        try {
            for (String word : words) {
                String key = alphabetize(word.toLowerCase());
                lexicon.computeIfAbsent(key, k -> new ArrayList<>()).add(word.toLowerCase());
            }
        } catch (Exception e) {
            throw new StorageException("Error while building lexicon", e);
        }
    }

    public List<String> getWordsWithSameLetters(String word) throws StorageException {
        try {
            String key = alphabetize(word.toLowerCase());
            return lexicon.getOrDefault(key, Collections.emptyList());
        } catch (Exception e) {
            throw new StorageException("Error while retrieving words with same letters", e);
        }
    }

    public String alphabetize(String word) {
        char[] chars = word.toLowerCase().toCharArray();
        Arrays.sort(chars);
        return new String(chars);
    }

    public String getAlphabetizeWord(String word) throws StorageException {
        try {
            return alphabetize(word);
        } catch (Exception e) {
            throw new StorageException("Error while alphabetizing word", e);
        }
    }
}