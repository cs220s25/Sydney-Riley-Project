package edu.moravian.exceptions;

public class InvalidCategoryException extends RuntimeException
{
    public InvalidCategoryException(String category)
    {
        super("The category " + category + " doesn't exist");
    }
}
