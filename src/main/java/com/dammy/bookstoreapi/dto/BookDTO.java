package com.dammy.bookstoreapi.dto;

import com.dammy.bookstoreapi.model.User;

public class BookDTO {
    private final Long id;
    private final String title;
    private final String genre;
    private final String isbn;
    private final String authorName;
    private final int publicationYear;

    public BookDTO(Long id, String title, String genre, String isbn, User author, int publicationYear) {
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.isbn = isbn;
        this.authorName = (author != null) ? author.getName() : "Unknown";
        this.publicationYear = publicationYear;
    }

    // Getters
    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getGenre() { return genre; }
    public String getIsbn() { return isbn; }
    public String getAuthorName() { return authorName; }
    public int getPublicationYear() { return publicationYear; }
}


