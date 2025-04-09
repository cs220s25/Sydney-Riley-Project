package edu.moravian.exceptions;

public class InvalidPlayerException extends RuntimeException
{
    public InvalidPlayerException(String player)
    {
        super("The player " + player + " doesn't exist");
    }
}
