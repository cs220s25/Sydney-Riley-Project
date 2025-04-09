package edu.moravian;

import edu.moravian.exceptions.*;
import org.junit.jupiter.api.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class WordScrambleGameTests
{
    @Test
    public void testNoInstanceNoGame() throws Exception
    {
        InMemory storage = new InMemory();
        storage.addCategory("category", List.of("word1", "word2"));
        WordScrambleGame game = new WordScrambleGame(storage, new Scrambler(), new InMemoryLexicon());
        assertFalse(game.gameInProgress());
        assertEquals(List.of("category"), game.getCategories());
    }

    @Test
    public void testBadCategoryThrowsException()
    {
        InMemory storage = new InMemory();
        WordScrambleGame game = new WordScrambleGame(storage, new Scrambler(), new InMemoryLexicon());
        assertThrows(InvalidCategoryException.class, () -> game.startGame("bad category", GameMode.BEST_OF_5, 1));
    }

    @Test
    public void testCanStartGame() throws Exception
    {
        InMemory storage = new InMemory();
        storage.addCategory("category", List.of("word1", "word2"));
        WordScrambleGame game = new WordScrambleGame(storage, new Scrambler(), new InMemoryLexicon());
        game.startGame("category", GameMode.BEST_OF_5, 5);
        assertTrue(game.gameInProgress());
        assertEquals("category", game.getCategory());
        assertEquals(List.of(), game.getPlayers());
        assertTrue(game.isWordInCategory("word1"));
        assertFalse(game.isWordInCategory("word3"));
        assertFalse(game.isUsed("word1"));
        assertFalse(game.isUsed("word3"));
    }

    @Test
    public void testGameCannotBeStartedWhileGameInProgress() throws Exception
    {
        InMemory storage = new InMemory();
        storage.addCategory("category", List.of("word1", "word2"));
        WordScrambleGame game = new WordScrambleGame(storage, new Scrambler(), new InMemoryLexicon());
        game.startGame("category", GameMode.BEST_OF_5, 5);
        assertThrows(GameInProgressException.class, () -> game.startGame("category", GameMode.BEST_OF_5, 2));
    }

    @Test
    public void testCanAddPlayers() throws Exception
    {
        InMemory storage = new InMemory();
        storage.addCategory("category", List.of("word1", "word2"));
        WordScrambleGame game = new WordScrambleGame(storage, new Scrambler(), new InMemoryLexicon());
        game.startGame("category", GameMode.BEST_OF_5, 5);
        game.addPlayer("player1");
        game.addPlayer("player2");
        assertEquals(List.of("player1", "player2"), game.getPlayers());
        assertEquals(List.of(), game.getWordsForPlayer("player1"));
        assertEquals(List.of(), game.getWordsForPlayer("player2"));
    }

    @Test
    public void testPlayersCanScoreUnusedWords() throws Exception {
        InMemory storage = new InMemory();
        storage.addCategory("category", List.of("word1", "word2"));
        WordScrambleGame game = new WordScrambleGame(storage, new Scrambler(), new InMemoryLexicon());
        game.startGame("category", GameMode.BEST_OF_5, 5);
        game.addPlayer("player1");
        game.addPlayer("player2");
        game.scoreWord("player1", "word1");
        game.scoreWord("player2", "word2");
        assertEquals(List.of("word1"), game.getWordsForPlayer("player1"));
        assertEquals(List.of("word2"), game.getWordsForPlayer("player2"));
        assertTrue(game.isUsed("word1"));
        assertTrue(game.isUsed("word2"));
    }

    @Test
    public void testCannotUseGameMethodsWhenNotInProgress() {
        InMemory storage = new InMemory();
        WordScrambleGame game = new WordScrambleGame(storage, new Scrambler(), new InMemoryLexicon());
        assertThrows(NoGameInProgressException.class, game::getCategory);
        assertThrows(NoGameInProgressException.class, () -> game.addPlayer("player"));
        assertThrows(NoGameInProgressException.class, game::getPlayers);
        assertThrows(NoGameInProgressException.class, () -> game.isWordInCategory("word"));
        assertThrows(NoGameInProgressException.class, () -> game.isUsed("word"));
        assertThrows(NoGameInProgressException.class, () -> game.scoreWord("player", "word"));
        assertThrows(NoGameInProgressException.class, () -> game.getWordsForPlayer("player"));
    }

    @Test
    public void testErrorToScoreUsedWord() throws Exception {
        InMemory storage = new InMemory();
        storage.addCategory("category", List.of("word1", "word2"));
        WordScrambleGame game = new WordScrambleGame(storage, new Scrambler(), new InMemoryLexicon());
        game.startGame("category", GameMode.BEST_OF_5, 5);
        game.addPlayer("player1");
        game.scoreWord("player1", "word1");
        assertThrows(WordAlreadyUsedException.class, () -> game.scoreWord("player1", "word1"));
    }

    @Test
    public void testErrorToScoreForUnknownPlayer() throws Exception
    {
        InMemory storage = new InMemory();
        storage.addCategory("category", List.of("word1", "word2"));
        WordScrambleGame game = new WordScrambleGame(storage, new Scrambler(), new InMemoryLexicon());
        game.startGame("category", GameMode.BEST_OF_5, 5);
        game.addPlayer("player1");
        assertThrows(InvalidPlayerException.class, () -> game.scoreWord("player2", "word1"));
    }

    @Test
    public void testErrorToScoreUnknownWord() throws Exception
    {
        InMemory storage = new InMemory();
        storage.addCategory("category", List.of("word1", "word2"));
        WordScrambleGame game = new WordScrambleGame(storage, new Scrambler(), new InMemoryLexicon());
        game.startGame("category", GameMode.BEST_OF_5, 5);
        game.addPlayer("player1");
        assertThrows(WordNotInCategoryException.class, () -> game.scoreWord("player1", "word3"));
    }

    @Test
    public void testErrorToGetWordsForUnknownPlayer() throws Exception
    {
        InMemory storage = new InMemory();
        storage.addCategory("category", List.of("word1", "word2"));
        WordScrambleGame game = new WordScrambleGame(storage, new Scrambler(), new InMemoryLexicon());
        game.startGame("category", GameMode.BEST_OF_5, 5);
        game.addPlayer("player1");
        assertThrows(InvalidPlayerException.class, () -> game.getWordsForPlayer("player2"));
    }

    @Test
    public void testResetClearsGameButLeavesCategories() throws Exception {
        InMemory storage = new InMemory();
        storage.addCategory("category", List.of("word1", "word2"));
        WordScrambleGame game = new WordScrambleGame(storage, new Scrambler(), new InMemoryLexicon());
        game.startGame("category", GameMode.BEST_OF_5, 5);
        game.addPlayer("player1");
        game.scoreWord("player1", "word1");
        game.reset();
        assertFalse(game.gameInProgress());
        assertEquals(List.of("category"), game.getCategories());
    }

    @Test
    public void testScoreWordNotInCategory() throws Exception {
        InMemory storage = new InMemory();
        storage.addCategory("category", List.of("word1", "word2"));
        WordScrambleGame game = new WordScrambleGame(storage, new Scrambler(), new InMemoryLexicon());
        game.startGame("category", GameMode.BEST_OF_5, 5);
        game.addPlayer("player1");
        assertThrows(WordNotInCategoryException.class, () -> game.scoreWord("player1", "word3"));
    }

    @Test
    public void testScoreWordForNonExistentPlayer() throws Exception {
        InMemory storage = new InMemory();
        storage.addCategory("category", List.of("word1", "word2"));
        WordScrambleGame game = new WordScrambleGame(storage, new Scrambler(), new InMemoryLexicon());
        game.startGame("category", GameMode.BEST_OF_5, 5);
        assertThrows(InvalidPlayerException.class, () -> game.scoreWord("player1", "word1"));
    }

    @Test
    public void testResetGame() throws Exception {
        InMemory storage = new InMemory();
        storage.addCategory("category", List.of("word1", "word2"));
        WordScrambleGame game = new WordScrambleGame(storage, new Scrambler(), new InMemoryLexicon());
        game.startGame("category", GameMode.BEST_OF_5, 5);
        game.addPlayer("player1");
        game.scoreWord("player1", "word1");
        game.reset();
        assertFalse(game.gameInProgress());
        assertEquals(List.of("category"), game.getCategories());
        assertThrows(NoGameInProgressException.class, game::getCategory);
    }

    @Test
    public void testStartGameWithInvalidRounds() throws Exception {
        InMemory storage = new InMemory();
        storage.addCategory("category", List.of("word1", "word2"));
        WordScrambleGame game = new WordScrambleGame(storage, new Scrambler(), new InMemoryLexicon());

        game.reset();
        assertThrows(IllegalArgumentException.class, () -> game.startGame("category", GameMode.BEST_OF_5, 0));

        game.reset();
        assertThrows(IllegalArgumentException.class, () -> game.startGame("category", GameMode.BEST_OF_5, 3));
    }

    @Test
    public void testPickNewScrambledWord() throws Exception {
        InMemory storage = new InMemory();
        storage.addCategory("category", List.of("word1", "word2"));
        WordScrambleGame game = new WordScrambleGame(storage, new Scrambler(), new InMemoryLexicon());
        game.startGame("category", GameMode.BEST_OF_5, 5);
        String scrambledWord = game.pickNewScrambledWord();
        assertNotNull(scrambledWord);
        assertNotEquals("", scrambledWord);
    }
}