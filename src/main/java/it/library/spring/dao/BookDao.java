/*
package it.library.spring.dao;

import it.library.spring.model.Book;
import org.springframework.stereotype.Repository;


import java.security.PublicKey;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static java.sql.DriverManager.*;

@Repository
public class BookDao {
    static final String DB_URL = "jdbc:mysql://localhost/library";
    static final String USER = "root";
    static final String PASS = "";

    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();

        try (Connection conn = getConnection(DB_URL, USER, PASS);
             Statement stmt = conn.createStatement()) {

            ResultSet rs = stmt.executeQuery("SELECT * FROM book");

            while (rs.next()) {
                int id = rs.getInt("id");
                String bookName = rs.getString("bookName");
                String bookAuthor = rs.getString("bookAuthor");
                String bookISBN = rs.getString("bookISBN");
                int year = rs.getInt("year");

                Book book = new Book(id, bookName, bookAuthor, bookISBN, year);
                books.add(book);
            }

        } catch (SQLException e) {
            System.err.println("Errore durante l'accesso al database: " + e.getMessage());
        }

        return books;
    }

    public Book getBookById(int id)  {
            String sql= "SELECT * FROM Book WHERE id= ?";
            Book book=null;
        try (Connection conn = getConnection(DB_URL, USER, PASS)) {
            try (PreparedStatement stmt = conn.prepareStatement(
                    sql)) {
                stmt.setInt(1, id);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        book = new Book(
                                rs.getInt("id"),
                                rs.getString("bookName"),
                                rs.getString("bookAuthor"),
                                rs.getString("bookISBN"),
                                rs.getInt("year")
                        );
                    }
                } catch (SQLException e) {
                    throw new RuntimeException("Id libro non trovato", e);
                }
                return book;
            } catch (SQLException e) {
                throw new RuntimeException("Impossibile eseguire la query sul db",e);
            }
        } catch (SQLException e) {
            throw new RuntimeException("problema di connessionesione al database", e);
        }
    }

    public Book getBookByISBN(String ISBN) {
        String sql = "SELECT * FROM Book WHERE bookISBN= ?";
        Book book = null;
        try (Connection conn = getConnection(DB_URL, USER, PASS)) {
            try (PreparedStatement stmt = conn.prepareStatement(
                    sql)) {
                stmt.setString(1, ISBN);

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        book = new Book(
                                rs.getInt("id"),
                                rs.getString("bookName"),
                                rs.getString("bookAuthor"),
                                rs.getString("bookISBN"),
                                rs.getInt("year")
                        );
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException("Impossibile eseguire la query sul db",e);
            }
            return book;
        } catch (SQLException e) {
            throw new RuntimeException("problema di connessionesione al database", e);
        }
    }

    public Book getBookByISBNPut(String ISBN, int id ) {
        String sql = "SELECT * FROM Book WHERE bookISBN= ? and id <> ?";
        Book book = null;
        try (Connection conn = getConnection(DB_URL, USER, PASS)) {
            try (PreparedStatement stmt = conn.prepareStatement(
                    sql)) {
                stmt.setString(1, ISBN);
                stmt.setInt(2, id);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        book = new Book(
                                rs.getInt("id"),
                                rs.getString("bookName"),
                                rs.getString("bookAuthor"),
                                rs.getString("bookISBN"),
                                rs.getInt("year")
                        );
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException("Impossibile eseguire la query sul db",e);
            }
            return book;
        } catch (SQLException e) {
            throw new RuntimeException("problema di connessionesione al database", e);
        }
    }



    public Book addBook(Book book)  {

       if (book.getBookName() == null || book.getBookName().trim().isEmpty()) {
            System.out.println("Nome libro non può essere vuoto");
            return null;
        }
        if (book.getBookAuthor() == null || book.getBookAuthor().trim().isEmpty()) {
            System.out.println("Nome autore non può essere vuoto");
            return null;
        }
        if (book.getBookISBN() == null || book.getBookISBN().trim().isEmpty()) {
            System.out.println("ISBN non può essere vuoto");
            return null;
        }
        if (book.getYear() <= 0) {
            System.out.println("Anno di pubblicazione non può essere un numero negativo e/o 0");
            return null;
        }

        String sql = "INSERT INTO book (bookName, bookAuthor, bookISBN, year) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection(DB_URL, USER, PASS);
             PreparedStatement stmt = conn.prepareStatement(sql)) {


            stmt.setString(1, book.getBookName());
            stmt.setString(2, book.getBookAuthor());
            stmt.setString(3, book.getBookISBN());
            stmt.setInt(4, book.getYear());


            int modifiedRows = stmt.executeUpdate();

            if (modifiedRows > 0) {
                System.out.println(modifiedRows + " record inseriti con successo.");
                return book;
            } else {
                System.out.println("Errore nell'inserimento del libro.");
                return null;
            }

        } catch (SQLException e) {
            System.err.println("Errore durante l'inserimento del libro: " + e.getMessage());

        }

        return null;
    }

    public void deleteBook(int id) {
        String sql = "DELETE FROM book WHERE ID = ?";

        try (Connection conn = getConnection(DB_URL, USER, PASS);
             PreparedStatement stmt = conn.prepareStatement(sql)) {


            stmt.setInt(1, id);


            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Lbro con ID " + id + " eliminato con successo.");
            } else {
                System.out.println("Nessun cliente trovato con l'ID: " + id);
            }

        } catch (SQLException e) {
            System.err.println("Errore durante l'eliminazione del libro: " + e.getMessage());
        }
    }

    public Book updateBook(Book book) {

        String sql = "UPDATE book SET  bookName= ?, bookAuthor = ?, bookISBN = ?, year = ? WHERE ID = ?";

        try (Connection conn = getConnection(DB_URL, USER, PASS);
             PreparedStatement stmt = conn.prepareStatement(sql)) {


            stmt.setString(1, book.getBookName());
            stmt.setString(2, book.getBookAuthor());
            stmt.setString(3, book.getBookISBN());
            stmt.setInt(4, book.getYear());
            stmt.setInt(5, book.getId());


            int rowsAffected = stmt.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Libro con ID " + book.getId() + " aggiornato con successo.");
                return book;
            } else {
                System.out.println("Nessun libro trovato con l'ID: " + book.getId());
                return null;
            }

        } catch (SQLException e) {
            System.err.println("Errore durante l'aggiornamento del Libro: " + e.getMessage());
        }

        return null;
    }

    public List<Book> filterBooks(Book book){
        List<Book> filteredBooksList = new ArrayList<>();
        String sql="SELECT * FROM Book WHERE bookName LIKE ? AND bookAuthor LIKE ? AND bookISBN LIKE ? ";
        try (Connection conn = getConnection(DB_URL, USER, PASS);
             PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setString(1,"%"+ book.getBookName()+"%");
            stmt.setString(2, "%"+book.getBookAuthor()+"%");
            stmt.setString(3,"%"+ book.getBookISBN()+"%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Book filteredBook = new Book(
                            rs.getInt("id"),
                            rs.getString("bookName"),
                            rs.getString("bookAuthor"),
                            rs.getString("bookISBN"),
                            rs.getInt("year")
                    );
                    filteredBooksList.add(filteredBook);
                }

            }catch (Exception e){
                System.err.println("impossibile eseguire query db: " + e.getMessage());
            }

        }catch (SQLException e){
            System.err.println("Errore connessione db: " + e.getMessage());
        }
        return  filteredBooksList;
    }
}

*/
