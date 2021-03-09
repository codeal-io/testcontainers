package io.codeal.testcontainers.model;

import java.util.Objects;

public class Book {

    private Integer id;
    private String title;
    private Integer year;
    private String author;

    public Book() {
    }

    public Book(final String title, final String author, final Integer year) {
        this.title = title;
        this.year = year;
        this.author = author;
    }

    public Book(final Integer id, final String title, final String author, final Integer year) {
        this.id = id;
        this.title = title;
        this.year = year;
        this.author = author;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(id, book.id) && Objects.equals(title, book.title)
                && Objects.equals(year, book.year)
                && Objects.equals(author, book.author);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, year, author);
    }
}
