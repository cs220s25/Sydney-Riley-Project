package edu.moravian;

import java.util.*;

public class BotResponses
{
    public static String serverError()
    {
        return "\nUh oh! Something went wrong on our end. Please try again later.";
    }

    public static String categories(List<String> categories) {
        StringBuilder response = new StringBuilder("\nThe available categories are:\n");
        for (String category : categories) {
            response.append("* ").append(category).append("\n");
        }
        return response.toString();
    }

    public static String gameStarted(String category, GameManager manager) {
        return "\nGame started! The category is: `" + category + "`. \n\n" +
                "The current game mode is: `" + manager.getMode() + "`. \n\n" +
                "Players can now join the game by typing `!join`. " +
                "When all players have joined, the game can begin by typing `!go`.";
    }

    public static String unknownCategory(String category)
    {
        return "\nThere is no category named `" + category.toLowerCase() + "`. " +
                "Please use `!categories` to see the available categories.";
    }

    public static String modes() {
        return "\nThe game modes are:\n" +
                "* " + GameMode.ENDLESS + "\n" +
                "* " + GameMode.BEST_OF_5;
    }

    public static String unknownMode(String mode)
    {
        return "There is no mode named " + mode + ".";
    }

    public static String missingMode() {
        return "\nThere is no mode currently selected. Please type in `!start <categories> <mode>`";
    }

    public static String invalidRounds() {
        return "\nThe number of rounds was either too big or small for the selected category.";
    }

    public static String gameAlreadyInProgress()
    {
        return "\nA game is already in progress. Use `!status` to see the current game status.";
    }

    public static String playerJoined(String username)
    {
        return "\n" + username + " has joined the game";
    }

    public static String noGameStarted()
    {
        return "\nNo game has been started.  Use `!start <category> <mode>` to start a new game.";
    }

    public static String goGame(String category, String scrambledWord, int currentRound)
    {
        return "\nThe category is: `" + category.toLowerCase() + "`.  " +
                "\n\nRound " + currentRound + ": \t" + "`" + scrambledWord + "`";
    }

    public static String noPlayersInGame()
    {
        return "\nNo players have joined the game. Use `!join` to join the game.";
    }

    public static String noGameStatus()
    {
        return "\nNo game is currently in progress.  Use `!start <category> <mode>` to start a new game.";
    }

    public static String gameStarting(String category, List<String> players)
    {
        return "\nA game is starting in category `" + category + "`.  " +
                "Players in the game: " + String.join(", ", players) + ".  " +
                "When all players have joined, the game can begin by typing `!go`.";
    }

    public static String gameInProgress(String category, HashMap<String, List<String>> playerWords)
    {
        StringBuilder response = new StringBuilder("\nA game is in progress in the category: \"`" + category + "`.\"\n\n");
        response.append("Current Scoreboard:\n");
        for (String player : playerWords.keySet())
        {
            response.append(player).append(": ").append(playerWords.get(player).size()).append(" words\n");
        }
        return response.toString();
    }

    public static String correctGuess(String username, String word)
    {
        return "\n" + username + " scores a point for guessing the word `" + word.toLowerCase() + "`!\n";
    }

    public static String  wrongGuess(String username, String word)
    {
        return "\n" + username + " your guess `" + word.toLowerCase() + "` is incorrect.";
    }

    public static String wordAlreadyUsed(String word)
    {
        return "\n`" + word.toLowerCase() + "` was already guessed. Hurry, guess again!";
    }

    public static String wordNotInCategory(String word, String category)
    {
        return  "\nIncorrect, `" + word.toLowerCase() + "` is not in the category `" + category + "`.";
    }

    public static String gameStartedButNotGoing()
    {
        return "\nA game has been started, and will begin by typing `!go`.";
    }

    public static String quitGame(String category, HashMap<String, List<String>> playerWords)
    {
        StringBuilder response = new StringBuilder("\nThe game is over.  Final results for the category: `" + category + "`.\n\n");
        response.append("Final Scoreboard:\n");
        int winningScore = 0;

        for (String player : playerWords.keySet())
        {
            if (winningScore < playerWords.get(player).size()) {
                winningScore = playerWords.get(player).size();
            }

            response.append(player).append(": ").append(playerWords.get(player).size()).append(" words\n");
        }

        response.append("\nThe Winner(s): " );
        for (String player : playerWords.keySet()) {
            if (playerWords.get(player).size() == winningScore) {
                response.append(player).append(" ");
            }
        }

        return response.toString();
    }

    public static String gameAbortedBeforeGo(String category)
    {
        return "\nThe game in category \"" + category + "\" has been aborted before it began.  " +
                "Use `!start <category> <mode>` to start a new game.";
    }

    public static String rules() {
        return """
                `Objective`: Be the first player to unscramble the scrambled word.

                 Scrambled: `pplea` \tUnscrambled: `apple`""";
    }

    public static String help()
    {
        return """
                * `~categories` - get a list of categories.\s
                * `!start <category> <mode>` - start a new game with specified category and selected game mode.
                * `!start random <mode>` - start a new game with a random category and selected game mode.
                * `!modes` - shows the different game modes.\s
                * `!join` - executed by any user who wants to join the game.
                * `!go` - used to start gameplay.\s
                * `!status` - get the status of the game.\s
                * `!quit` - ends the game and shows results\s
                * `!rules` - explains the rules of the game\s
                * `!help` - list the commands and basic explanation\s
                """;
    }
}
