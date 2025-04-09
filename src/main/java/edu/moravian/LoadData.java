package edu.moravian;

import edu.moravian.exceptions.*;
import redis.clients.jedis.*;
import java.io.*;
import java.nio.file.*;
import java.util.*;

public class LoadData
{

    public static List<String> readFile(String fileName) throws IOException
    {
        Path filePath = Paths.get(fileName);
        return Files.readAllLines(filePath);
    }

    public static void main(String[] args)
    {
        try
        {
            Redis redisActions = new Redis("localhost", 6379);
            Lexicon lexiconActions = new RedisLexicon(new Jedis("localhost", 6379));

            redisActions.resetToEmpty();

            redisActions.addCategory("animals", readFile("src/data/animals.txt"));
            System.out.println("animals added");
            redisActions.addCategory("cities", readFile("src/data/cities.txt"));
            System.out.println("cities added");
            redisActions.addCategory("demo", readFile("src/data/demo.txt"));
            System.out.println("demo added");

            lexiconActions.buildLexicon(readFile("src/data/animals.txt"));
            System.out.println("animals added to Redis");
            lexiconActions.buildLexicon(readFile("src/data/cities.txt"));
            System.out.println("cities added to Redis");
            lexiconActions.buildLexicon(readFile("src/data/demo.txt"));
            System.out.println("demo added to Redis");
        }
        catch (StorageException|IOException e)
        {
            System.out.println("Error while reading files");
        }
    }
}
