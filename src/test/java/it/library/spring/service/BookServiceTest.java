package it.library.spring.service;


import it.library.spring.entity.Book;
import it.library.spring.model.repository.BookRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.when;

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


        book =  new Book("Il gran vivone", "Pasqualino", "9780141036136", 1197);
        /*book.setId(40);*/
    }

    @Test
    public void testGetBookById() {

        when(bookRepository.findById(anyInt())).thenReturn(book);
        Book bookFoundById = bookService.getBookById(1);
        System.out.println(bookFoundById);
        assertEquals(book.getBookISBN(), bookFoundById.getBookISBN());
    }

}
