package edu.moravian.exceptions;

public class NoGameInProgressException extends RuntimeException
{
    public NoGameInProgressException()
    {
        super("There is no game currently in progress");
    }
}
