package it.library.spring.exception;

public class BookISBNConflictException extends RuntimeException{
    public BookISBNConflictException(String message){
        super(message);
    }
}
