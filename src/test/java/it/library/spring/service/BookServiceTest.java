package it.library.spring.service;

import it.library.spring.entity.Book;
import it.library.spring.model.repository.BookRepository;
import it.library.spring.model.specifications.BookSpecifications;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.domain.Specification;

import java.util.Arrays;
import java.util.List;

import static it.library.spring.model.specifications.BookSpecifications.isWithin;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class BookServiceTest {

    @Mock
    BookRepository bookRepository;



    @InjectMocks
    BookService bookService;

    private Book book;

    @Before
    public void setUp() {
        book = new Book("Il gran vivone", "Pasqualino", "9780141036136", 1197);
    }

    @Test
    public void addBookValidISBN() {
        Book bookToAdd = new Book("Il gran vivone", "Pasqualino", "9780141036136", 1197);
        bookToAdd.setId(anyInt());

        when(bookRepository.findByBookISBN(bookToAdd.getBookISBN())).thenReturn(null);
        when(bookRepository.save(bookToAdd)).thenReturn(bookToAdd);

        Book addedBook = bookService.addBook(bookToAdd);
        assertNotNull(addedBook);
    }

    @Test
    public void testGetBookById() {
        when(bookRepository.findById(anyInt())).thenReturn(book);
        Book bookFoundById = bookService.getBookById(1);
        assertEquals(book.getBookISBN(), bookFoundById.getBookISBN());
    }

    @Test public void getAllBooksSuccess(){
        List<Book> bookList = Arrays.asList(
                new Book("Il gran vivone", "Pasqualino", "9780141036136", 1197),
                new Book("La casa del piacere", "Maria Rossi", "9780141036143", 256)
        );
        when(bookRepository.findAll()).thenReturn(bookList);
        List allBooks = bookService.getAllBooks();
        assertNotNull(allBooks);

    }

    @Test
    public void updateBook() {
        Book oldBook = new Book("Il vecchio libro", "Autore", "9780141036136", 1197);
        oldBook.setId(30);
        Book newBook = new Book("Il nuovo libro", "Autore", "9780141036136", 1197);
        newBook.setId(30);

        when(bookRepository.findByBookISBNAndIdNot(newBook.getBookISBN(), newBook.getId())).thenReturn(null);
        when(bookRepository.findById(oldBook.getId())).thenReturn(oldBook);
        when(bookRepository.save(newBook)).thenReturn(newBook);

        Book updatedBook = bookService.updateBook(newBook);

        assertNotNull(updatedBook);
        assertEquals(newBook.getBookName(), updatedBook.getBookName());
        assertEquals(newBook.getBookAuthor(), updatedBook.getBookAuthor());
        assertEquals(newBook.getBookISBN(), updatedBook.getBookISBN());
        assertEquals(newBook.getId(), updatedBook.getId());
    }

    @Before
    public void setUpBookToDelete() {
        book = new Book("Il gran vivone", "Pasqualino", "9780141036136", 1197);
        book.setId(1);
    }

    @Test
    public void deleteBook() {
        when(bookRepository.findById(1)).thenReturn(book);
        bookService.deleteBook(1);
        verify(bookRepository, times(1)).delete(1);

        when(bookRepository.findById(1)).thenReturn(null);
        Book emptyBook = bookRepository.findById(1);
        assertNull(emptyBook);

    }

    @Test
    public void filterBookSuccess(){
        List<Book> bookList = Arrays.asList(
                new Book("Il gran vivone", "Pasqualino", "9780141036136", 1197),
                new Book("La casa del piacere", "Maria Rossi", "9780141036143", 256),
                new Book("L'arte della guerra", "Sun Tzu", "9780141036150", 132),
                new Book("Il codice Da Vinci", "Dan Brown", "9780141036167", 689),
                new Book("La ragazza del treno", "Paula Hawkins", "9780141036174", 395),
                new Book("1984", "George Orwell", "9780141036181", 328)
        );
        Book bookToFind = new Book("1984", "George Orwell", "9780141036181", 328);

        when(bookRepository.findAll(any(Specification.class))).thenReturn(Arrays.asList(bookToFind));

         List<Book> bookListFound = bookService.filterBooks(bookToFind);
        assertNotNull(bookListFound);

    }


}
