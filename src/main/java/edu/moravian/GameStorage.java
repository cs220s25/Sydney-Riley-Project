package edu.moravian;

import edu.moravian.exceptions.*;
import java.util.*;

/**
 * This interface defines methods for storing and retrieving data for the Categories game.
 * This interfaces does NOT validate the data being stored or retrieved - i.e. it does NOT
 * implement game logic.  The query methods  will return an empty list, empty string, or
 * false if the parameters refer to data that has not been stored.
 */
public interface GameStorage
{
    /**
     * Resets the category to ""
     * @throws StorageException if the storage mechanism fails
     */
    void resetCategory() throws StorageException;

    /**
     * Resets the player list to an empty list
     * @throws StorageException if the storage mechanism fails
     */
    void resetPlayers() throws StorageException;

    /**
     * Add a category to the storage.
     * @param categoryName the name of the category
     * @param words the words in the category
     * @throws StorageException if the storage mechanism fails
     */
    void addCategory(String categoryName, List<String> words) throws StorageException;

    /**
     * Get the list of all categories in the storage. Returns an empty list if there are no categories.
     * @return a list of all categories in the storage
     * @throws StorageException if the storage mechanism fails
     */
    List<String> getCategories() throws StorageException;

    /**
     * Set the current category.
     * @param category the name of the category
     * @throws StorageException if the storage mechanism fails
     */
    void setCategory(String category) throws StorageException;

    /**
     * Get the current category. Returns an empty string if there is no current category.
     * @return the name of the current category
     * @throws StorageException if the storage mechanism fails
     */
    String getCategory() throws StorageException;

    /**
     * Record a player and an empty list of words for that player.  If the player already exists, this method
     * will overwrite the player's list of words with a new empty list.
     * @param playerName the name of the player
     * @throws StorageException if the storage mechanism fails
     */
    void addPlayer(String playerName) throws StorageException;

    /**
     * Get the list of all players in the storage.  Returns an empty list if there are no players.
     * @return a list of all players in the storage
     * @throws StorageException if the storage mechanism fails
     */
    List<String> getPlayers() throws StorageException;

    /**
     * Get the list of words for a player. Returns an empty list if the player does not exist or has no words.
     * @param playerName the name of the player
     * @return a list of words for the player
     * @throws StorageException if the storage mechanism fails
     */
    List<String> getPlayerWords(String playerName) throws StorageException;

    /**
     * Get the list of words from selected category.
     * @return a list of words from selected category
     * @throws StorageException if the storage mechanism fails
     */
    List<String> getCategoryWords(String category) throws StorageException;


    /**
     * Check if a word is in a category. Returns false if the category or word does not exist.
     * @param word the word to check
     * @param category the category to check
     * @return true if the word is in the category, false otherwise
     * @throws StorageException if the storage mechanism fails
     */
    boolean isWordInCategory(String word, String category) throws StorageException;

    /**
     * Check if a word is in a player's list.  Returns false if the player or word does not exist.
     *
     * @param player the player to check
     * @param word the word to check
     * @return true if the word is in the player's list, false otherwise
     * @throws StorageException if the storage mechanism fails
     */
    boolean isWordInPlayerList(String player, String word) throws StorageException;

    /**
     * Add a word to a player's list. If the player does not exist, this method will create the player
     * first and then add the word to the player's list.
     *
     * @param player the player to add the word to
     * @param word the word to add
     * @throws StorageException if the storage mechanism fails
     */
    void addWordToPlayerList(String player, String word) throws StorageException;
}
