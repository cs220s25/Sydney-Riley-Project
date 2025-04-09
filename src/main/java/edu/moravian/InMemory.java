package edu.moravian;

import java.util.*;

/**
 * This class provides an in-memory implementation of the GameStorage interface.
 */
public class InMemory implements GameStorage {
    private final HashMap<String, List<String>> categories;
    private final HashMap<String, List<String>> playerWords;
    private String currentCategory;

    /**
     * Create a new InMemory instance that contains no categories, no players/words, and
     * no current category.
     */
    public InMemory() {
        categories = new HashMap<>();
        playerWords = new HashMap<>();
        currentCategory = "";
    }

    @Override
    public void resetCategory() {
        currentCategory = "";
    }

    @Override
    public void resetPlayers() {
        playerWords.clear();
    }

    @Override
    public void addCategory(String categoryName, List<String> words) {
        List<String> lowerCaseWords = new ArrayList<>();
        for (String word : words) {
            lowerCaseWords.add(word.toLowerCase());
        }
        categories.put(categoryName.toLowerCase(), lowerCaseWords);
    }

    @Override
    public List<String> getCategories() {
        List<String> ret = new LinkedList<>(categories.keySet());
        Collections.sort(ret);
        return ret;
    }

    @Override
    public void setCategory(String category) {
        currentCategory = category.toLowerCase();
    }

    @Override
    public String getCategory() {
        return currentCategory;
    }

    @Override
    public void addPlayer(String playerName) {
        playerWords.put(playerName.toLowerCase(), new LinkedList<>());
    }

    @Override
    public List<String> getPlayers() {
        return List.copyOf(playerWords.keySet());
    }

    @Override
    public List<String> getPlayerWords(String playerName) {
        return playerWords.getOrDefault(playerName.toLowerCase(), List.of());
    }

    @Override
    public List<String> getCategoryWords(String category) {
        return categories.getOrDefault(category.toLowerCase(), List.of());
    }

    @Override
    public boolean isWordInCategory(String word, String category) {
        String lowerCaseWord = word.toLowerCase();
        for (String categoryWord : categories.getOrDefault(category.toLowerCase(), List.of())) {
            if (categoryWord.toLowerCase().equals(lowerCaseWord)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isWordInPlayerList(String player, String word) {
        String lowerCaseWord = word.toLowerCase();
        for (String playerWord : playerWords.getOrDefault(player.toLowerCase(), List.of())) {
            if (playerWord.toLowerCase().equals(lowerCaseWord)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void addWordToPlayerList(String player, String word) {
        if (!playerWords.containsKey(player.toLowerCase())) {
            addPlayer(player.toLowerCase());
        }
        playerWords.get(player.toLowerCase()).add(word.toLowerCase());
    }
}