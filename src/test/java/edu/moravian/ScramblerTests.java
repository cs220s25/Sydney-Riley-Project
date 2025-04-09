package edu.moravian;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class ScramblerTests {

    @Test
    public void testNewScrambleWord() {
        Scrambler scrambler = new Scrambler();
        assertEquals("", scrambler.getUnscrambledWord());
    }

    @Test
    public void testSetAndGetUnscrambledWord() {
        Scrambler scrambler = new Scrambler();
        scrambler.setUnscrambledWord("Horse");
        assertEquals("horse", scrambler.getUnscrambledWord());
    }

    @Test
    public void testScrambleWordNoSpaces() {
        Scrambler scrambler = new Scrambler();
        scrambler.setUnscrambledWord("Horse");
        String scrambled = scrambler.scrambleWord("Horse");
        assertNotEquals("Horse", scrambled);
        assertEquals("horse", scrambler.getUnscrambledWord());
    }

    @Test
    public void testScrambleWordMultipleTimes() {
        Scrambler scrambler = new Scrambler();
        scrambler.setUnscrambledWord("Horse");
        String firstScramble = scrambler.scrambleWord("Horse");
        String secondScramble = scrambler.scrambleWord("Horse");
        assertNotEquals(firstScramble, secondScramble);
    }
}