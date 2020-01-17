package se.alten.schoolproject.exceptions;

public class DuplicateEmail extends Exception{
    public DuplicateEmail(String errorMessage,
    Throwable error) {
        super (errorMessage, error);
    }
}
