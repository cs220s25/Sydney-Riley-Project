package edu.moravian.exceptions;

public class GameInProgressException extends RuntimeException
{
    public GameInProgressException()
    {
        super("There is a game already in progress");
    }
}
