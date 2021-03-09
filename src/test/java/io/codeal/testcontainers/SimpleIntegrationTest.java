package io.codeal.testcontainers;

import io.codeal.testcontainers.model.Book;
import io.codeal.testcontainers.repository.BookRepository;
import org.jdbi.v3.core.Jdbi;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.File;
import java.time.Duration;

@Testcontainers
public class SimpleIntegrationTest {
    public static final String JDBC_URL_TEMPLATE = "jdbc:postgresql://%s:%d/codeal";
    public static final String DB_USER = "codeal";
    public static final String DB_PASSWORD = "codeal";

    @Container
    public static DockerComposeContainer environment =
            new DockerComposeContainer(new File("src/test/resources/docker-compose-test.yml"))
                    .withLocalCompose(true)
                    .withExposedService("postgres", 5432,
                            Wait.forListeningPort().withStartupTimeout(Duration.ofSeconds(30)));

    private static Jdbi jdbi;
    private static BookRepository bookRepository;

    @BeforeAll
    public static void setup() {
        final var host = environment.getServiceHost("postgres", 5432);
        final var port = environment.getServicePort("postgres", 5432);

        final var jdbcUrl = String.format(JDBC_URL_TEMPLATE, host, port);
        jdbi = Jdbi.create(jdbcUrl, DB_USER, DB_PASSWORD);
        bookRepository = new BookRepository(jdbi);

        final var book = new Book("Test containers", "Codeal.IO", 2021);
        bookRepository.save(book);
    }

    @Test
    @DisplayName("Should save Book into database")
    public void shouldSaveBookTest() {
        final var book = new Book("Test book 2", "Codeal.IO", 2021);

        bookRepository.save(book);

        final var savedBook = jdbi.withHandle(handle ->
                handle.select("SELECT * FROM book WHERE title = :title AND author = :author and year = :year")
                        .bind("author", book.getAuthor())
                        .bind("title", book.getTitle())
                        .bind("year", book.getYear())
                        .map((rs, ctx) -> new Book(rs.getInt("id"),
                                rs.getString("title"),
                                rs.getString("author"),
                                rs.getInt("year")))
                        .one()
        );

        Assertions.assertNotNull(savedBook.getId());
    }

    @Test
    @DisplayName("Should find Book by id")
    public void shouldFindBookByIdTest() {
        final var testBook = bookRepository.findById(1);

        Assertions.assertTrue(testBook.isPresent());
        final var book = testBook.get();
        Assertions.assertNotNull(book.getId());
        Assertions.assertEquals(book.getTitle(), "Test containers");
        Assertions.assertEquals(book.getAuthor(), "Codeal.IO");
        Assertions.assertEquals(book.getYear(), 2021);
    }
}
