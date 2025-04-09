package edu.moravian;

import edu.moravian.exceptions.*;
import java.util.*;

/**
 * Manages the game state and interactions with the WordScrambleGame.
 * Enforces decoupling to separate the dependency on WordScrambleGame for BotResponder.
 */
public class GameManager {
    private final WordScrambleGame game;
    private GameState state;
    private GameMode mode;

    /**
     * Constructs a new GameManager.
     * @param game the WordScrambleGame instance to use
     */
    public GameManager(WordScrambleGame game) {
        this.game = game;
        this.state = GameState.NO_GAME;
        this.mode = GameMode.DEFAULT;
    }

    /**
     * Gets the list of categories in the game.
     * @return a list of categories in the game
     * @throws InternalServerException if there is an internal error
     */
    public List<String> getCategories() throws InternalServerException {
        return game.getCategories();
    }

    /**
     * Starts a game with the specified category and game mode.
     * Ensures that the game continues until currentRound reaches the totalRounds for the selected game mode.
     * @param category the category to use
     * @param mode the game mode to use
     * @param totalRounds the total number of rounds
     * @throws InternalServerException if there is an internal error
     * @throws IllegalArgumentException if the number of rounds is invalid
     */
    public void startGame(String category, GameMode mode, int totalRounds) throws InternalServerException {
        game.startGame(category, mode, totalRounds);
        this.mode = mode;
        state = GameState.STARTING;
    }

    /**
     * Adds a player to the storage to keep track of the words each player guesses correctly first.
     * @param username the name of the player
     * @throws InternalServerException if there is an internal error
     */
    public void addPlayer(String username) throws InternalServerException {
        game.addPlayer(username);
    }

    /**
     * Increments the current round by one.
     */
    public void incrementRound() {
        game.incrementRound();
    }

    /**
     * Checks if the game is in progress.
     * @return true if the game is in progress, false otherwise
     */
    public boolean isGameInProgress() {
        return state == GameState.IN_PROGRESS;
    }

    /**
     * Checks if the game is starting.
     * @return true if the game is starting, false otherwise
     */
    public boolean isGameStarting() {
        return state == GameState.STARTING;
    }

    /**
     * Checks if there is no active game.
     * @return true if there is no active game, false otherwise
     */
    public boolean isNoGame() {
        return state == GameState.NO_GAME;
    }

    /**
     * Sets the game state to in progress.
     */
    public void setGameInProgress() {
        state = GameState.IN_PROGRESS;
    }

    /**
     * Resets all components of the game when the game is finished or quit.
     * @throws InternalServerException if there is an internal error
     * @throws StorageException if there is an error with the storage
     */
    public void resetGame() throws InternalServerException, StorageException {
        game.reset();
        state = GameState.NO_GAME;
        this.mode = GameMode.DEFAULT;
    }

    /**
     * Gets the current category for the game.
     * @return the current category in the game
     * @throws InternalServerException if there is an internal error
     */
    public String getCategory() throws InternalServerException {
        return game.getCategory();
    }

    /**
     * Gets the list of players that joined the game.
     * @return a list of players that joined the game
     * @throws InternalServerException if there is an internal error
     */
    public List<String> getPlayers() throws InternalServerException {
        return game.getPlayers();
    }

    /**
     * Gets the list of words for a specific player.
     * @param player the name of the player
     * @return a list of words for the player
     * @throws InternalServerException if there is an internal error
     */
    public List<String> getWordsForPlayer(String player) throws InternalServerException {
        return game.getWordsForPlayer(player);
    }

    /**
     * Checks if a guessed word is in the selected game category.
     * @param word the guessed word by the player
     * @return true if the guessed word is in the category, false otherwise
     * @throws InternalServerException if there is an internal error
     */
    public boolean isWordInCategory(String word) throws InternalServerException {
        return game.isWordInCategory(word);
    }

    /**
     * Checks if a guessed word has already been correctly guessed.
     * @param word the guessed word by the player
     * @return true if the guessed word was already guessed, false otherwise
     * @throws InternalServerException if there is an internal error
     */
    public boolean isUsed(String word) throws InternalServerException {
        return game.isUsed(word);
    }

    /**
     * Adds a guessed word to the player's word list and prevents the word from being guessed or used again in the game.
     * @param player the player that has joined the game
     * @param word the guessed word by the player
     * @throws InternalServerException if there is an internal error
     * @throws StorageException if there is an error with the storage
     */
    public void scoreWord(String player, String word) throws InternalServerException, StorageException {
        game.scoreWord(player, word);
    }

    /**
     * Gets a new word for players to guess.
     * @return a randomly picked word from the selected game category
     * @throws InternalServerException if there is an internal error
     * @throws StorageException if there is an error with the storage
     */
    public String pickNewScrambledWord() throws InternalServerException, StorageException {
        return game.pickNewScrambledWord();
    }

    /**
     * Gets the current round number.
     * @return the current round number
     */
    public int getCurrentRound() {
        return game.getCurrentRound();
    }

    /**
     * Gets the total number of rounds played for the selected game mode.
     * @return the total rounds played by the game based on the game mode
     */
    public int getTotalRounds() {
        return game.getTotalRounds();
    }

    /**
     * Checks whether all words from the category have been picked.
     * @return true if all words from the game category have been used, false otherwise
     * @throws InternalServerException if there is an internal error
     * @throws StorageException if there is an error with the storage
     */
    public boolean allWordsUsed() throws InternalServerException, StorageException {
        return game.allWordsUsed();
    }

    /**
     * Checks if a guessed word matches the unscrambled version of the scrambled word.
     * @param word the guessed word by the player
     * @return true if the player's guess is correct, false otherwise
     * @throws InternalServerException if there is an internal error
     * @throws StorageException if there is an error with the storage
     */
    public boolean checkGuess(String word) throws InternalServerException, StorageException {
        return game.isGuessCorrect(word);
    }

    /**
     * Gets the total number of words in a game category.
     * @param category the selected category of the game
     * @return the number of words within the game category
     * @throws InternalServerException if there is an internal error
     */
    public int getTotalWordsInCategory(String category) throws InternalServerException {
        try {
            return game.getTotalWordsInCategory(category);
        } catch (StorageException e) {
            throw new InternalServerException("Error while getting total words in category");
        }
    }

    /**
     * Gets the current game mode.
     * @return the current game mode
     */
    public GameMode getMode() {
        return mode;
    }
}