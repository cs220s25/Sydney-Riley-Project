package edu.moravian;

import edu.moravian.exceptions.*;
import net.dv8tion.jda.api.*;
import net.dv8tion.jda.api.entities.channel.middleman.*;
import net.dv8tion.jda.api.events.message.*;
import net.dv8tion.jda.api.hooks.*;
import net.dv8tion.jda.api.requests.*;
import org.jetbrains.annotations.*;
import redis.clients.jedis.*;
import redis.clients.jedis.exceptions.*;

public class WordScrambleBot {
    /**
     * Sets up the environment necessary for the Discord Bot to run and play Word Scramble Game.
     */
    public static void main(String[] args) {
        Redis storage = createStorage();
        Lexicon lexicon = createLexicon();
        WordScrambleGame game = new WordScrambleGame(storage, new Scrambler(), lexicon);
        GameManager gameManager = new GameManager(game);
        BotResponder responder = new BotResponder(gameManager);
        String token = loadToken();
        startBot(responder, token);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                gameManager.resetGame();
                System.out.println("Game reset successfully.");
            } catch (InternalServerException | StorageException e) {
                System.err.println("Error while resetting the game: " + e.getMessage());
            }
        }));
    }

    private static Redis createStorage() {
        Redis storage = null;
        try {
            storage = new Redis("localhost", 6379);
        } catch (Exception e) {
            System.err.println("Failed to connect to Redis\n\nIs it running?");
            System.exit(1);
        }
        return storage;
    }

    private static Lexicon createLexicon() {
        Lexicon lexicon = null;
        try {
            Jedis jedis = new Jedis("localhost", 6379);
            lexicon = new RedisLexicon(jedis);
            jedis.ping();
        } catch (JedisException e) {
            System.err.println("Failed to connect to Redis\n\nIs it running?");
            System.exit(1);
        }
        return lexicon;
    }

    private static String loadToken() {
        try {
            String secretName = "220_Discord_Token";
            String secretKey = "DISCORD_TOKEN";

            Secrets secrets = new Secrets();

            String secret = secrets.getSecret(secretName, secretKey);
            return secret;
        } catch (SecretsException e) {
            System.out.println(e.getMessage());
            System.exit(1);
            return null;
        }
    }

    private static void startBot(BotResponder responder, String token) {
        JDA api = JDABuilder.createDefault(token).enableIntents(GatewayIntent.MESSAGE_CONTENT).build();

        api.addEventListener(new ListenerAdapter() {
            @Override
            public void onMessageReceived(@NotNull MessageReceivedEvent event) {
                if (event.getAuthor().isBot())
                    return;

                if (!event.getChannel().getName().equals("discord-bot"))
                    return;

                String username = event.getAuthor().getName();
                String message = event.getMessage().getContentRaw();
                MessageChannel channel = event.getChannel();

                String response = responder.respond(username, message, channel);
                if (response != null && !response.trim().isEmpty()) {
                    channel.sendMessage(response).queue();
                }
            }
        });
    }
}