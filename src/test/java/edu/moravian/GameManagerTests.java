package edu.moravian;

import org.junit.jupiter.api.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class GameManagerTests {
    private GameManager gameManager;

    @BeforeEach
    void setUp() {
        InMemory storage = new InMemory();
        storage.addCategory("category", List.of("word1", "word2"));
        WordScrambleGame game = new WordScrambleGame(storage, new Scrambler(), new InMemoryLexicon());
        gameManager = new GameManager(game);
    }

    @Test
    void testStartGame() throws Exception {
        gameManager.startGame("category", GameMode.BEST_OF_5, 5);
        assertTrue(gameManager.isGameStarting());
        assertEquals("category", gameManager.getCategory());
    }

    @Test
    void testAddPlayer() throws Exception {
        gameManager.startGame("category", GameMode.BEST_OF_5, 5);
        gameManager.addPlayer("player1");
        assertEquals(List.of("player1"), gameManager.getPlayers());
    }

    @Test
    void testScoreWord() throws Exception {
        gameManager.startGame("category", GameMode.BEST_OF_5, 5);
        gameManager.addPlayer("player1");
        gameManager.scoreWord("player1", "word1");
        assertEquals(List.of("word1"), gameManager.getWordsForPlayer("player1"));
    }

    @Test
    void testResetGame() throws Exception {
        gameManager.startGame("category", GameMode.BEST_OF_5, 5);
        gameManager.addPlayer("player1");
        gameManager.scoreWord("player1", "word1");
        gameManager.resetGame();
        assertFalse(gameManager.isGameInProgress());
        assertEquals(List.of("category"), gameManager.getCategories());
    }

    @Test
    void testGetTotalWordsInCategory() throws Exception {
        gameManager.startGame("category", GameMode.BEST_OF_5, 5);
        int totalWords = gameManager.getTotalWordsInCategory("category");
        assertEquals(2, totalWords);
    }
}