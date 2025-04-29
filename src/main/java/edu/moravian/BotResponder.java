package edu.moravian;

import edu.moravian.exceptions.*;
import net.dv8tion.jda.api.entities.channel.middleman.*;
import java.util.*;

public class BotResponder {
    private final GameManager manager;
    private String currentScrambledWord;

    public BotResponder(GameManager manager) {
        this.manager = manager;
    }

    public String respond(String username, String message, MessageChannel channel) {
        try {
            if (message.equals("@categories"))
                return handleCategories();
            else if (message.startsWith("!start"))
                return handleStart(message);
            else if (message.startsWith("!modes"))
                return handleMode();
            else if (message.equals("!join"))
                return handleJoin(username);
            else if (message.equals("!go"))
                return handleGo(channel);
            else if (message.equals("!status"))
                return handleStatus();
            else if (message.equals("!quit"))
                return handleQuit();
            else if (message.equals("!rules"))
                return handleRules();
            else if (message.equals("!help"))
                return handleHelp();
            else
                return handleWord(username, message, channel);
        } catch (InternalServerException e) {
            return BotResponses.serverError();
        } catch (StorageException e) {
            throw new RuntimeException(e);
        }
    }

    private String handleCategories() throws InternalServerException {
        return BotResponses.categories(manager.getCategories());
    }

    private String handleStart(String startMessage) throws InternalServerException {
        if (!manager.isNoGame()) {
            return BotResponses.gameAlreadyInProgress();
        }

        String[] parts = startMessage.split(" ");
        if (parts.length < 3) {
            return BotResponses.missingMode();
        }

        String category = parts[1].toLowerCase();
        if (category.equals("random")) {
            category = manager.getCategories().get((int) (Math.random() * manager.getCategories().size()));
        } else if (!manager.getCategories().contains(category)) {
            return BotResponses.unknownCategory(category);
        }

        String mode = parts[2].toUpperCase();
        GameMode gameMode;
        int totalRounds;

        if (!mode.equals("ENDLESS") && !mode.equals("BEST_OF_5") && !mode.equals("DEFAULT")) {
            return BotResponses.unknownMode(mode);
        }

        if (mode.equals("ENDLESS")) {
            gameMode = GameMode.ENDLESS;
            totalRounds = manager.getTotalWordsInCategory(category);
        } else if (mode.equals("BEST_OF_5")) {
            gameMode = GameMode.BEST_OF_5;
            totalRounds = 5;
        } else {
            return BotResponses.missingMode();
        }

        try {
            manager.startGame(category, gameMode, totalRounds);
        } catch (IllegalArgumentException e) {
            return BotResponses.invalidRounds();
        }

        return BotResponses.gameStarted(category, manager);
    }

    private String handleMode() throws InternalServerException {
        return BotResponses.modes();
    }

    private String handleJoin(String username) throws InternalServerException {
        if (manager.isNoGame())
            return BotResponses.noGameStarted();

        manager.addPlayer(username);
        return BotResponses.playerJoined(username);
    }

    private String handleGo(MessageChannel channel) throws InternalServerException, StorageException {
        if (manager.isNoGame()) {
            return BotResponses.noGameStarted();
        }

        if (manager.isGameStarting()) {
            if (manager.getPlayers().isEmpty()) {
                return BotResponses.noPlayersInGame();
            }
            manager.setGameInProgress();
        }

        manager.incrementRound();
        if (manager.getCurrentRound() > manager.getTotalRounds() || manager.allWordsUsed()) {
            return handleQuit();
        }

        currentScrambledWord = manager.pickNewScrambledWord();
        channel.sendMessage(BotResponses.goGame(manager.getCategory(), currentScrambledWord, manager.getCurrentRound())).queue();
        return "";
    }

    private String handleStatus() throws InternalServerException {
        if (manager.isNoGame())
            return BotResponses.noGameStatus();
        else if (manager.isGameStarting())
            return BotResponses.gameStarting(manager.getCategory(), manager.getPlayers());

        HashMap<String, List<String>> playerWords = new HashMap<>();
        for (String player : manager.getPlayers())
            playerWords.put(player, manager.getWordsForPlayer(player));

        return BotResponses.gameInProgress(manager.getCategory(), playerWords);
    }

    private String handleQuit() throws InternalServerException, StorageException {
        if (manager.isNoGame()) {
            return BotResponses.noGameStatus();
        }

        if (manager.isGameStarting()) {
            String category = manager.getCategory();
            manager.resetGame();
            return BotResponses.gameAbortedBeforeGo(category);
        }

        HashMap<String, List<String>> playerWords = new HashMap<>();
        for (String player : manager.getPlayers()) {
            playerWords.put(player, manager.getWordsForPlayer(player));
        }

        String ret = BotResponses.quitGame(manager.getCategory(), playerWords);
        manager.resetGame();
        return ret;
    }

    private static String handleRules() {
        return BotResponses.rules();
    }

    private static String handleHelp() {
        return BotResponses.help();
    }

    private String handleWord(String username, String word, MessageChannel channel) throws InternalServerException, StorageException {
        if (manager.isNoGame())
            return BotResponses.noGameStarted();
        if (manager.isGameStarting())
            return BotResponses.gameStartedButNotGoing();
        if (manager.isUsed(word.toLowerCase()))
            return BotResponses.wordAlreadyUsed(word);
        if (!manager.isWordInCategory(word.toLowerCase()))
            return BotResponses.wordNotInCategory(word, manager.getCategory());

        if (!manager.checkGuess(word.toLowerCase()) || word.equalsIgnoreCase(currentScrambledWord)) {
            return BotResponses.wrongGuess(username, word);
        }

        manager.scoreWord(username, word.toLowerCase());
        channel.sendMessage(BotResponses.correctGuess(username, word)).queue();

        if (manager.getCurrentRound() < manager.getTotalRounds() && !manager.allWordsUsed()) {
            channel.sendMessage("\n---------------------------------------------------------------------------\n").queue();
            handleGo(channel);
        } else {
            HashMap<String, List<String>> playerWords = new HashMap<>();

            for (String player : manager.getPlayers()) {
                playerWords.put(player, manager.getWordsForPlayer(player));
            }

            channel.sendMessage("\n---------------------------------------------------------------------------\n").queue();
            String endResponse = BotResponses.quitGame(manager.getCategory(), playerWords);
            manager.resetGame();
            channel.sendMessage(endResponse).queue();
        }
        return "";
    }
}