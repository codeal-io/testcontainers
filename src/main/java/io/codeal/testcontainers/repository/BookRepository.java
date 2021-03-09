package io.codeal.testcontainers.repository;

import io.codeal.testcontainers.model.Book;
import org.jdbi.v3.core.Jdbi;

import java.util.Optional;

public class BookRepository {

    public static final String INSERT_BOOK_SQL = "INSERT INTO book (title, author, year) VALUES (:title, :author, :year)";
    public static final String FIND_BY_ID_SQL = "SELECT * FROM book where ID = ?";
    private final Jdbi jdbi;

    public BookRepository(final Jdbi jdbi) {
        this.jdbi = jdbi;
    }

    public Optional<Book> findById(final long id) {
        return jdbi.withHandle(handle -> handle.createQuery(FIND_BY_ID_SQL)
                .bind(0, id)
                .mapToBean(Book.class)
                .findOne());
    }

    public void save(final Book book) {
        jdbi.withHandle(handle -> handle.createUpdate(INSERT_BOOK_SQL)
                .bind("title", book.getTitle())
                .bind("author", book.getAuthor())
                .bind("year", book.getYear())
                .execute());
    }
}
