package it.library.spring.model.specifications;

import it.library.spring.entity.Book;
import org.springframework.data.jpa.domain.Specification;


import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;


public class BookSpecifications {

    public static Specification<Book> isWithin(Book book){
        return (root, query, builder) -> {
            List<Predicate> bookPredicate = new ArrayList<>();
            if(book.getBookAuthor()!=null){
                bookPredicate.add(builder.like(root.get("bookAuthor"),"%"+ book.getBookAuthor()+ "%"));
            }
            if(book.getBookISBN()!=null){
                bookPredicate.add(builder.like(root.get("bookISBN"),"%"+ book.getBookISBN()+ "%"));
            }
            if(book.getBookName()!=null){
                bookPredicate.add(builder.like(root.get("bookName"),"%"+ book.getBookName()+ "%"));
            }
            return builder.and(bookPredicate.toArray(new Predicate[0]));
        };
    }

}
