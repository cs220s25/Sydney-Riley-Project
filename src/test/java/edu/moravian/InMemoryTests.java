package edu.moravian;

import org.junit.jupiter.api.*;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

class InMemoryTests
{
    @Test
    public void testNewInstance()
    {
        InMemory memoryStorage = new InMemory();
        assertEquals(List.of(), memoryStorage.getCategories());
        assertEquals("", memoryStorage.getCategory());
        assertEquals(List.of(), memoryStorage.getPlayers());
        assertEquals(List.of(), memoryStorage.getPlayerWords("nonexistent"));
        assertFalse(memoryStorage.isWordInCategory("nonexistentCategory", "nonexistentWord"));
        assertFalse(memoryStorage.isWordInPlayerList("nonexistentUser", "nonexistentWord"));
    }

    @Test
    public void testResetCategory()
    {
        InMemory memoryStorage = new InMemory();
        memoryStorage.addCategory("category1", List.of("word1", "word2"));
        memoryStorage.setCategory("category1");
        memoryStorage.resetCategory();
        assertEquals("", memoryStorage.getCategory());
    }

    @Test
    public void testResetPlayers()
    {
        InMemory memoryStorage = new InMemory();
        memoryStorage.addPlayer("player1");
        memoryStorage.addWordToPlayerList("player1", "word1");
        memoryStorage.resetPlayers();
        assertEquals(List.of(), memoryStorage.getPlayers());
        assertEquals(List.of(), memoryStorage.getPlayerWords("player1"));
    }

    @Test
    public void testCanAddCategory()
    {
        InMemory memoryStorage = new InMemory();
        memoryStorage.addCategory("category1", List.of("word1", "word2"));
        assertEquals(List.of("category1"), memoryStorage.getCategories());
        assertTrue(memoryStorage.isWordInCategory("word1", "category1"));
        assertTrue(memoryStorage.isWordInCategory("word2", "category1"));
        assertFalse(memoryStorage.isWordInCategory("word3", "category1"));
    }

    @Test
    public void testCanSetCategory()
    {
        InMemory memoryStorage = new InMemory();
        memoryStorage.addCategory("category1", List.of("word1", "word2"));
        memoryStorage.setCategory("category1");
        assertEquals("category1", memoryStorage.getCategory());
    }

    @Test
    public void testCanAddPlayers()
    {
        InMemory memoryStorage = new InMemory();
        memoryStorage.addPlayer("player1");
        assertEquals(List.of("player1"), memoryStorage.getPlayers());
    }

    @Test
    public void testCanAddWordsToPlayerLists()
    {
        InMemory memoryStorage = new InMemory();
        memoryStorage.addPlayer("player1");
        memoryStorage.addWordToPlayerList("player1", "word1");
        memoryStorage.addWordToPlayerList("player1", "word2");
        assertEquals(List.of("word1", "word2"), memoryStorage.getPlayerWords("player1"));
    }

    @Test
    public void testAddWordBeforeAddPlayerSucceeds()
    {
        InMemory memoryStorage = new InMemory();
        memoryStorage.addWordToPlayerList("player1", "word1");
        assertEquals(List.of("word1"), memoryStorage.getPlayerWords("player1"));
    }

    @Test
    public void testAddPlayerWhenTheyAlreadyHaveWordsResetsWordListToEmpty()
    {
        InMemory memoryStorage = new InMemory();
        memoryStorage.addPlayer("player1");
        memoryStorage.addWordToPlayerList("player1", "word1");
        memoryStorage.addPlayer("player1");
        assertEquals(List.of(), memoryStorage.getPlayerWords("player1"));
    }

    @Test
    public void testAddAndRetrieveCategory() {
        InMemory storage = new InMemory();
        storage.addCategory("category", List.of("word1", "word2"));
        assertEquals(List.of("category"), storage.getCategories());
        assertEquals(List.of("word1", "word2"), storage.getCategoryWords("category"));
    }

    @Test
    public void testAddAndRetrievePlayer() {
        InMemory storage = new InMemory();
        storage.addPlayer("player1");
        assertEquals(List.of("player1"), storage.getPlayers());
    }

    @Test
    public void testAddAndRetrievePlayerWords() {
        InMemory storage = new InMemory();
        storage.addPlayer("player1");
        storage.addWordToPlayerList("player1", "word1");
        assertEquals(List.of("word1"), storage.getPlayerWords("player1"));
    }
}