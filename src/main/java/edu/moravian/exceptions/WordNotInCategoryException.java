package edu.moravian.exceptions;

public class WordNotInCategoryException extends RuntimeException
{
    public WordNotInCategoryException(String word)
    {
        super("Word " + word + " is not in the category");
    }
}
