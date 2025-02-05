package it.library.spring.exception;

public class InvalidISBNException extends RuntimeException {
    public InvalidISBNException(String message){
        super(message);
    }
}
