package it.library.spring.controller;

import it.library.spring.entity.Book;
import it.library.spring.service.BookService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyInt;
import static org.springframework.http.ResponseEntity.status;


@SpringBootTest
@AutoConfigureMockMvc
@RunWith(MockitoJUnitRunner.class)
public class BookControllerTest {


    @Mock
    private BookService bookService;


    @InjectMocks
    private BookController bookController;



    @Autowired
    private MockMvc mockMvc;






    @Test
    public void testGetAllBooks() throws Exception {
        List<Book> books = Arrays.asList(
                new Book("La storia di tutti", "Dariuccio", "9890824129404", 2021),
                new Book("La storia di Pasquale", "Pask", "9890824129403", 2024)
        );

        Mockito.when(bookService.getAllBooks()).thenReturn(books);
        /*this.mockMvc.perform(MockMvcRequestBuilders.get("/getAllBooks").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());*/
        ResponseEntity<List<Book>> response =bookController.getAllBooks();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(bookController.getAllBooks());

    }



    @Test
    public void getById(){
        List<Book> books = new ArrayList<>(Arrays.asList(
                new Book("La storia di tutti", "Dariuccio", "9890824129404", 2021),
                new Book("La storia di Pasquale", "Pask", "9890824129403", 2024)
        ));
        books.get(0).setId(1);
        books.get(1).setId(2);
        Mockito.when(bookService.getBookById(1)).thenReturn(books.get(0));

        ResponseEntity<Book> resultById = bookController.getBookById(1);
        Mockito.verify(bookService).getBookById(1);
        assertEquals(HttpStatus.OK, resultById.getStatusCode());
        assertNotNull(resultById);

    }
    @Test
    public void addBook(){
        Book bookToAdd = new Book("Il viaggio nel tempo", "Marco Rossi", "9890824129400", 2023);
        bookToAdd.setId(anyInt());
        Mockito.when(bookService.addBook(bookToAdd)).thenReturn(bookToAdd);
        ResponseEntity <Book> result = bookController.addBook(bookToAdd);
        assertEquals(HttpStatus.CREATED, result.getStatusCode());
        Mockito.verify(bookService, Mockito.times(1)).addBook(bookToAdd);
    }

    @Test
    public void updateBook()
    {
        Book bookToUpdate = new Book("Il viaggio nel tempo", "Marco Rossi", "9890824129400", 2023);
        bookToUpdate.setId(anyInt());
        Mockito.when(bookService.updateBook(bookToUpdate)).thenReturn(bookToUpdate);
        ResponseEntity <Book> result = bookController.updateBook(bookToUpdate);
        Mockito.verify(bookService, Mockito.times(1)).updateBook(bookToUpdate);


        assertNotNull(result);
    }

    @Test
    public void testDeleteBook(){
        List<Book> books = new ArrayList<>(Arrays.asList(
                new Book("La storia di tutti", "Dariuccio", "9890824129404", 2021),
                new Book("La storia di Pasquale", "Pask", "9890824129403", 2024)
        ));
        books.get(0).setId(1);
        books.get(1).setId(2);
        Mockito.doNothing().when(bookService).deleteBook(1); //doNothing perchè la delete non ritorna nulla, il then non è possibile

        ResponseEntity<Void> response = bookController.deleteBook(1);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        Mockito.verify(bookService).deleteBook(1);
        Mockito.verify(bookService, Mockito.never()).deleteBook(2);
        Mockito.verify(bookService, Mockito.times(1)).deleteBook(1);
    }

    @Test
    public void filterBooks(){
        Book filters = new Book(null, "Marco Rossi", "98", null);

        List<Book> filteredBooks = Arrays.asList(
                new Book("Il viaggio nel tempo", "Marco Rossi", "980012345678", 2023),
                new Book("Avventure di ieri", "Marco Rossi", "980023456789", 2022),
                new Book("Un viaggio straordinario", "Marco Rossi", "980012345678", 2021)
        );
        Mockito.when(bookService.filterBooks(filters)).thenReturn(filteredBooks);
        ResponseEntity<List<Book>> filterResult = bookController.filterBooks(filters);

        Mockito.verify(bookService).filterBooks(filters);
        assertEquals(HttpStatus.OK, filterResult.getStatusCode());
        assertNotNull(filterResult);

    }
}
