package edu.moravian.exceptions;

public class WordAlreadyUsedException extends RuntimeException
{
    public WordAlreadyUsedException(String word)
    {
        super("The word " + word + " has already been used in this game");
    }
}
