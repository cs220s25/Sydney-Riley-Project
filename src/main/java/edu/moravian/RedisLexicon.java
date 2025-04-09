package edu.moravian;

import edu.moravian.exceptions.*;
import redis.clients.jedis.*;
import redis.clients.jedis.exceptions.*;
import java.util.*;

public class RedisLexicon implements Lexicon {
    private final Jedis jedis;

    public RedisLexicon(Jedis jedis) {
        this.jedis = jedis;
    }

    public void buildLexicon(List<String> words) throws StorageException {
        try {
            for (String word : words) {
                String key = alphabetize(word.toLowerCase());
                jedis.sadd(key, word.toLowerCase());
            }
        } catch (JedisException e) {
            throw new StorageException("Error while building lexicon");
        }
    }

    public List<String> getWordsWithSameLetters(String word) throws StorageException {
        try {
            String key = alphabetize(word.toLowerCase());
            Set<String> words = jedis.smembers(key);
            return new ArrayList<>(words);
        } catch (JedisException e) {
            throw new StorageException("Error while retrieving words with same letters");
        }
    }

    public String alphabetize(String word) {
        char[] chars = word.toLowerCase().toCharArray();
        Arrays.sort(chars);
        return new String(chars);
    }

    public String getAlphabetizeWord(String word) throws StorageException {
        try {
            return alphabetize(word);
        } catch (Exception e) {
            throw new StorageException("Error while alphabetizing word", e);
        }
    }
}