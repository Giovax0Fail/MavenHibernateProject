package it.library.spring.service;

import it.library.spring.entity.Book;
import it.library.spring.exception.BookISBNConflictException;
import it.library.spring.exception.InvalidISBNException;
/*import it.library.spring.model.Book;*/
import it.library.spring.model.repository.BookRepository;
import it.library.spring.model.specifications.BookSpecifications;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import it.library.spring.exception.BookNotFoundException;
import java.util.List;
import java.util.MissingFormatWidthException;



@Service("bookService")
public class BookService {
   /* @Autowired
    BookDao bookDao;*/
    @Autowired
    BookRepository bookRepository;


    public List<Book> getAllBooks() {
        List<Book> bookList = bookRepository.findAll();

        if(bookList.isEmpty()){
            throw new BookNotFoundException("Libri non trovati nel DB");
        }
        for (Book book : bookList) {
            System.out.println(book);
        }
        return bookList;
    }

    public Book getBookById(int id) {
        Book book = bookRepository.findById(id);
        if (book == null) {

            throw new BookNotFoundException("Libro con " + id + "non trovato" );
        }
        return book;
    }



    public Book addBook(Book book) {
        if(!isValidISBN(book.getBookISBN())){
            throw new InvalidISBNException("L'ISBN " + book.getBookISBN() + "non è valido");
        }
        Book existingBook = bookRepository.findByBookISBN(book.getBookISBN());
        if (existingBook != null) {
            throw new BookISBNConflictException("Il libro con ISBN " + book.getBookISBN() + " è già presente.");
        }


        return bookRepository.save(book);

    };

    public Book updateBook(Book book) {
        if (!isValidISBN(book.getBookISBN())) {
            throw new InvalidISBNException("L'ISBN " + book.getBookISBN() + "non è valido");
        }
        Book existingBook = bookRepository.findByBookISBNAndIdNot(book.getBookISBN(),book.getId().intValue());
        if (existingBook != null) {
            throw new BookISBNConflictException("Il libro con ISBN " + book.getBookISBN() + " è già presente.");
        }
        Book bookFoundById=bookRepository.findById(book.getId());
        mapBook(book, bookFoundById);

        bookRepository.save(bookFoundById);
       return bookFoundById;
    }

    private static void mapBook(Book book, Book bookFoundById) {
        bookFoundById.setBookAuthor(book.getBookAuthor());
        bookFoundById.setBookISBN(book.getBookISBN());
        bookFoundById.setYear(book.getYear());
        bookFoundById.setBookName(book.getBookName());
    }

    public void deleteBook(Integer id) {
        Book existingBook = bookRepository.findById(id);
        if(existingBook==null) {
            throw new BookNotFoundException("Il libro con id" + id + "non esiste");
        }else{
            bookRepository.delete(id);
        }
    }

    public Book checkISBN(Book book){
        if (!isValidISBN(book.getBookISBN())) {
            throw new InvalidISBNException("L'ISBN " + book.getBookISBN() + "non è valido");
        }
        Book existingBook= bookRepository.findByBookISBN(book.getBookISBN());

        if (existingBook!=null){
            throw new BookISBNConflictException("L'ISBN" + book.getBookISBN() + " è già presente");
        }
        return book;


    }

    private boolean isValidISBN(String bookISBN) {
        String isbnRegex = "^(?:\\d{9}[\\dX]|\\d{13})$";
        return bookISBN.matches(isbnRegex);
    }

    public List<Book> filterBooks(Book book) {
        checkEmptyString(book);
        List<Book> filteredBooks = bookRepository.findAll(BookSpecifications.isWithin(book)); //metodo statico non ha bisogno di ogg
        if(filteredBooks.isEmpty()){
            System.out.println("Libro con i caratteri interessati non trovato");
        }

            return filteredBooks;
    }

    private void checkEmptyString(Book book){
        if(book.getBookName()==null){
            book.setBookName("");
        }
        if(book.getBookAuthor()==null){
            book.setBookAuthor("");
        }
        if(book.getBookISBN()==null){
            book.setBookISBN("");
        }
    }


}
