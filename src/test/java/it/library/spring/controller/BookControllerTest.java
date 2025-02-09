package it.library.spring.controller;

import it.library.spring.entity.Book;
import it.library.spring.service.BookService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.ui.Model;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class BookControllerTest {


    @Mock
    private BookService bookService;


    @InjectMocks
    private BookController bookController;





    @Test
    public void testGetAllBooks()  {
        List<Book> books = Arrays.asList(
                new Book("La storia di tutti", "Dariuccio", "9890824129404", 2021),
                new Book("La storia di Pasquale", "Pask", "9890824129403", 2024)
        );


        Mockito.when(bookService.getAllBooks()).thenReturn(books);
        assertNotNull(bookController.getAllBooks());

    }
}
