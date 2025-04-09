 package edu.moravian;

import edu.moravian.exceptions.*;
import java.util.*;

public class WordScrambleGame {
    private final GameStorage storage;
    private final Scrambler scrambler;
    private final Lexicon lexicon;
    private final List<String> usedWords;
    private GameMode mode;
    private int totalRounds;
    private int currentRound;
    private String category;

    public WordScrambleGame(GameStorage storage, Scrambler scrambler, Lexicon lexicon) {
        this.storage = storage;
        this.scrambler = scrambler;
        this.lexicon = lexicon;
        this.usedWords = new ArrayList<>();
        this.mode = GameMode.DEFAULT;
        this.category = "";
    }

    public void startGame(String category, GameMode mode, int totalRounds) throws InternalServerException {
        if (gameInProgress()) {
            throw new GameInProgressException();
        }

        if (!getCategories().contains(category)) {
            throw new InvalidCategoryException(category);
        }

        try {
            storage.setCategory(category);
            this.category = category;
            this.mode = mode;
            this.usedWords.clear();

            if (this.mode == GameMode.ENDLESS) {
                this.totalRounds = Integer.MAX_VALUE;
            } else if (this.mode == GameMode.BEST_OF_5) {
                if (totalRounds != 5) {
                    throw new IllegalArgumentException("BEST_OF_5 mode must have exactly 5 rounds.");
                }
                this.totalRounds = totalRounds;
            } else {
                throw new IllegalArgumentException("Invalid game mode.");
            }
            this.currentRound = 0;

        } catch (StorageException e) {
            throw new InternalServerException("Error while starting the game");
        }
    }

    public void incrementRound() {
        this.currentRound++;
    }

    public int getTotalRounds() {
        return this.totalRounds;
    }

    public int getCurrentRound() {
        return this.currentRound;
    }

    public boolean gameInProgress() throws InternalServerException {
        try {
            return !storage.getCategory().isEmpty();
        } catch (StorageException e) {
            throw new InternalServerException("Error while checking game in progress");
        }
    }

    public List<String> getCategories() throws InternalServerException {
        try {
            return storage.getCategories();
        } catch (StorageException e) {
            throw new InternalServerException("Error while getting categories");
        }
    }

    public String getCategory() throws InternalServerException {
        if (!gameInProgress()) {
            throw new NoGameInProgressException();
        }

        try {
            return storage.getCategory();
        } catch (StorageException e) {
            throw new InternalServerException("Error while getting category");
        }
    }

    public void addPlayer(String player) throws InternalServerException {
        if (!gameInProgress()) {
            throw new NoGameInProgressException();
        }
        try {
            storage.addPlayer(player);
        } catch (StorageException e) {
            throw new InternalServerException("Error while adding player");
        }
    }

    public List<String> getPlayers() throws InternalServerException {
        if (!gameInProgress()) {
            throw new NoGameInProgressException();
        }

        try {
            return storage.getPlayers();
        } catch (StorageException e) {
            throw new InternalServerException("Error while getting players");
        }
    }

    public boolean isWordInCategory(String word) throws InternalServerException {
        if (!gameInProgress()) {
            throw new NoGameInProgressException();
        }

        try {
            List<String> categoryWords = storage.getCategoryWords(this.category);
            return categoryWords.contains(word);
        } catch (StorageException e) {
            throw new InternalServerException("Error while checking word");
        }
    }

    public boolean isUsed(String word) throws InternalServerException {
        if (!gameInProgress()) {
            throw new NoGameInProgressException();
        }

        try {
            String letterForm = lexicon.getAlphabetizeWord(word.toLowerCase().trim());
            return usedWords.contains(letterForm);
        } catch (StorageException e) {
            throw new InternalServerException("Error while checking if word is used");
        }
    }

    public void scoreWord(String player, String guessedWord) throws StorageException, InternalServerException {
        if (!gameInProgress()) {
            throw new NoGameInProgressException();
        }

        if (!storage.getPlayers().contains(player)) {
            throw new InvalidPlayerException(player);
        }

        String formattedWord = guessedWord.toLowerCase().trim();
        if (!isWordInCategory(formattedWord)) {
            throw new WordNotInCategoryException(guessedWord);
        }

        if (isUsed(formattedWord)) {
            throw new WordAlreadyUsedException(guessedWord);
        }

        storage.addWordToPlayerList(player, guessedWord);
        usedWords.add(lexicon.getAlphabetizeWord(formattedWord));
    }

    public List<String> getWordsForPlayer(String player) throws InternalServerException {
        if (!gameInProgress()) {
            throw new NoGameInProgressException();
        }

        try {
            if (!storage.getPlayers().contains(player)) {
                throw new InvalidPlayerException(player);
            }
            return storage.getPlayerWords(player);
        } catch (StorageException e) {
            throw new InternalServerException("Error while getting words for player");
        }
    }

    public String getWordFromCategory() throws StorageException, InternalServerException {
        Random random = new Random();
        String category = getCategory();
        List<String> words = storage.getCategoryWords(category);
        return words.get(random.nextInt(words.size()));
    }

    public boolean isGuessCorrect(String word) throws InternalServerException, StorageException {
        if (!gameInProgress()) {
            throw new NoGameInProgressException();
        }

        String guessedWord = word.toLowerCase().trim();
        List<String> categoryWords = storage.getCategoryWords(this.category);

        for (String categoryWord : categoryWords) {
            if (categoryWord.toLowerCase().trim().equals(guessedWord)) {
                return true;
            }
        }
        return false;
    }

    public String pickNewScrambledWord() throws StorageException, InternalServerException {
        String word = getWordFromCategory().toLowerCase().trim();
        String letterForm = lexicon.getAlphabetizeWord(word);
        String category = getCategory();
        List<String> words = storage.getCategoryWords(category);
        Random random = new Random();

        while (usedWords.contains(letterForm)) {
            word = words.get(random.nextInt(words.size())).toLowerCase().trim();
            letterForm = lexicon.getAlphabetizeWord(word);
        }

        scrambler.setUnscrambledWord(word);
        return scrambler.scrambleWord(word);
    }

    public boolean allWordsUsed() throws StorageException, InternalServerException {
        String category = getCategory();
        List<String> words = storage.getCategoryWords(category);
        for (String word : words) {
            if (!usedWords.contains(lexicon.getAlphabetizeWord(word.toLowerCase().trim()))) {
                return false;
            }
        }
        return true;
    }

    public void reset() throws InternalServerException {
        try {
            storage.resetCategory();
            storage.resetPlayers();
            usedWords.clear();
            currentRound = 0;
            totalRounds = 0;
            mode = GameMode.DEFAULT;
            category = "";
        } catch (StorageException e) {
            throw new InternalServerException("Error while resetting the game");
        }
    }

    public int getTotalWordsInCategory(String category) throws StorageException {
        List<String> words = storage.getCategoryWords(category);
        return words.size();
    }
}