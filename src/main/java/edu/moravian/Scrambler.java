package edu.moravian;

import java.util.*;

/**
 This class takes the given word selected under a category and scrambles the word
 for players to guess. It checks verifies if a player correctly guesses the word.
 */
public class Scrambler {
    private String unscrambledWord;

    public Scrambler() {
        unscrambledWord = "";
    }

    /**
     * Scrambles that letters of the unscrambledWord.
     * @param unscrambledWord Word randomly picked from selected category.
     * @return Scrambled version of unscrambledWord.
     */
    public String scrambleWord(String unscrambledWord) {

        List<Character> characters = new ArrayList<>();
        StringBuilder scrambledWord = new StringBuilder();

        for (char character : unscrambledWord.toCharArray()) {
            characters.add(character);
        }

        Collections.shuffle(characters);

        for (int i = 0; i < unscrambledWord.length(); i++) {
            scrambledWord.append(characters.get(0));
            characters.remove(0);
        }

        return scrambledWord.toString();
    }

    /**
     * Sets the unscrambled word with given word.
     * @param word Unscrambled word.
     */
    public void setUnscrambledWord(String word) {
        this.unscrambledWord = word.toLowerCase();
    }

    /**
     * Gets the unscrambled version of scrambled word
     * @return Unscrambled version of scrambled word.
     */
    public String getUnscrambledWord() {
        return this.unscrambledWord;
    }
}
