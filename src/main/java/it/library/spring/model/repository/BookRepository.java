package it.library.spring.model.repository;

import it.library.spring.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


public interface BookRepository extends JpaRepository<Book, Integer>, JpaSpecificationExecutor<Book> {
    Book findById(int id);
    Book findByBookISBN(String bookISBN);
    Book findByBookISBNAndIdNot(String bookISBN,int id);

}