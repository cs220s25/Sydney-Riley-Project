package edu.moravian;

import edu.moravian.exceptions.*;
import redis.clients.jedis.*;
import java.util.*;

/**
 * This class provides a redis implementation of the GameStorage interface.
 */
public class Redis implements GameStorage {
    private final Jedis jedis;

    /**
     * Creates a new Redis instance that connects to the specified Redis server.
     */
    public Redis(String host, int port) {
        this.jedis = new Jedis(host, port);
    }

    @Override
    public void resetCategory() {
        jedis.del("gameCategory");
    }

    @Override
    public void resetPlayers() {
        jedis.del("players");
        jedis.del("playerWords");
    }

    @Override
    public void addCategory(String categoryName, List<String> words) {
        jedis.sadd("categories", categoryName.toLowerCase());
        for (String word : words) {
            jedis.sadd("category:" + categoryName.toLowerCase(), word.toLowerCase());
        }
    }

    @Override
    public List<String> getCategories() {
        List<String> categories = new LinkedList<>(jedis.smembers("categories"));
        Collections.sort(categories);
        return categories;
    }

    @Override
    public void setCategory(String category) {
        jedis.set("gameCategory", category.toLowerCase());
    }

    @Override
    public String getCategory() {
        String category = jedis.get("gameCategory");
        return category == null ? "" : category;
    }

    @Override
    public void addPlayer(String playerName) {
        jedis.sadd("players", playerName.toLowerCase());
        jedis.del("playerWords:" + playerName.toLowerCase());
    }

    @Override
    public List<String> getPlayers() {
        return new LinkedList<>(jedis.smembers("players"));
    }

    @Override
    public List<String> getPlayerWords(String playerName) {
        return new LinkedList<>(jedis.smembers("playerWords:" + playerName.toLowerCase()));
    }

    @Override
    public List<String> getCategoryWords(String category) {
        return new LinkedList<>(jedis.smembers("category:" + category.toLowerCase()));
    }

    @Override
    public boolean isWordInCategory(String word, String category) {
        return jedis.sismember("category:" + category.toLowerCase(), word.toLowerCase());
    }

    @Override
    public boolean isWordInPlayerList(String player, String word) {
        return jedis.sismember("playerWords:" + player.toLowerCase(), word.toLowerCase());
    }

    @Override
    public void addWordToPlayerList(String player, String word) {
        jedis.sadd("playerWords:" + player.toLowerCase(), word.toLowerCase());
    }

    /**
     * Resets the Redis storage
     * @throws StorageException if the storage mechanism fails
     */
    public void resetToEmpty() throws StorageException {
        resetCategory();
        resetPlayers();
    }
}