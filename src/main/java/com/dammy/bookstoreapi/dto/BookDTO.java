package com.dammy.bookstoreapi.dto;

public class BookDTO {
    private final Long id;
    private final String title;
    private final String genre;
    private final String isbn;
    private final UserDTO author;
    private final int publicationYear;

    public BookDTO(Long id, String title, String genre, String isbn, UserDTO author, int publicationYear) {
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.isbn = isbn;
        this.author = author;
        this.publicationYear = publicationYear;
    }

    // Getters
    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getGenre() { return genre; }
    public String getIsbn() { return isbn; }
    public UserDTO getAuthor() { return author; }
    public int getPublicationYear() { return publicationYear; }
}