package it.library.spring.controller;
import it.library.spring.entity.Book;
import it.library.spring.exception.BookNotFoundException;


import it.library.spring.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.CustomAutowireConfigurer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.sql.*;
import java.util.ArrayList;

@CrossOrigin(origins ="http://localhost:4200", methods = {RequestMethod.GET,RequestMethod.POST,RequestMethod.PUT,RequestMethod.DELETE})
@RestController
public class BookController {
    @Autowired
    BookService bookService;

    @RequestMapping(value = "/getAllBooks", method = RequestMethod.GET, headers = "Accept=application/json")
    public ResponseEntity<List<Book>> getAllBooks(){
        List<Book> listOfBooks = bookService.getAllBooks();
        /*model.addAttribute("book",new Book());
        model.addAttribute("listofBooks", listOfBooks);*/
        return new ResponseEntity<>(listOfBooks, HttpStatus.OK);
    }
    @RequestMapping(value = "/", method = RequestMethod.GET, headers = "Accept=application/json")
    public String goToHomePage() {
        return "redirect:/getAllBooks";
    }

    @RequestMapping(value="/{id}", method=RequestMethod.GET, headers = "Accept=application/json")
    public ResponseEntity<Book> getBookById(@PathVariable("id") int id) {
        Book book = bookService.getBookById(id);
        if (book == null) {

            throw new BookNotFoundException("Libro con " + id + "   non trovato");
        }
        return new ResponseEntity<>(book,HttpStatus.OK);
    }
    @RequestMapping(value = "/addBook", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity <Book> addBook(@RequestBody  Book book) {
        System.out.println("Ricevuto libro per l'inserimento: ");
        System.out.println("Nome: " + book.getBookName());
        System.out.println("Autore: " + book.getBookAuthor());
        System.out.println("ISBN: " + book.getBookISBN());
        System.out.println("Anno: " + book.getYear());
        System.out.println("Ricevuto libro per l'inserimento: " + book);
        Book addedBook = bookService.addBook(book);
        if (addedBook != null) {
            System.out.println("Libro aggiunto con successo: " + addedBook);
        } else {
            System.out.println("Errore nell'aggiunta del libro.");
        }
        System.out.println("Libro aggiunto con successo: " + addedBook);
        return new ResponseEntity<>(addedBook,HttpStatus.CREATED);
    }

    @RequestMapping(value = "/addBook", method = RequestMethod.PUT, headers = "Accept=application/json")
    public ResponseEntity <Book> updateBook(@RequestBody Book book) {
        Book updatedBook = bookService.updateBook(book);
        return new ResponseEntity<>(updatedBook,HttpStatus.OK);

    }

    @RequestMapping(value = "/deleteBook/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
    public  ResponseEntity<Void> deleteBook (@PathVariable("id") Integer id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();

    }

    @RequestMapping(value = "/filterBook", method = RequestMethod.POST, headers = "Accept=application/json")
    public ResponseEntity<List<Book>> filterBooks(@RequestBody Book book) {
        List<Book> filteredBooks = bookService.filterBooks(book);
        return new ResponseEntity<>(filteredBooks, HttpStatus.OK);
    }
}


