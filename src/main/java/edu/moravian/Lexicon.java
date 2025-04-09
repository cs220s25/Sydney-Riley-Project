package edu.moravian;

import edu.moravian.exceptions.*;
import java.util.*;

public interface Lexicon {
    /**
     * Builds lexicon in given storage system
     * @throws StorageException if the storage mechanism fails
     */
    void buildLexicon(List<String> words) throws StorageException;

    /**
     * Returns words that use the same letters
     * @throws StorageException if the storage mechanism fails
     */
    List<String> getWordsWithSameLetters(String word) throws StorageException;

    /**
     * Alphabetizes the letters in a given word
     */
    String alphabetize(String word);

    /**
     * Returns word that is letter alphabetized
     * @throws StorageException if the storage mechanism fails
     */
    String getAlphabetizeWord(String word) throws StorageException;

}
