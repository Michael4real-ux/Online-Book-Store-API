package com.dammy.bookstoreapi.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public record BookDTO(Long id, String title, String genre, String isbn, UserDTO author, int publicationYear,
                      double price) {
    @JsonCreator
    public BookDTO(
            @JsonProperty("id") Long id,
            @JsonProperty("title") String title,
            @JsonProperty("genre") String genre,
            @JsonProperty("isbn") String isbn,
            @JsonProperty("author") UserDTO author,
            @JsonProperty("publicationYear") int publicationYear,
            @JsonProperty("price") double price
    ) {
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.isbn = isbn;
        this.author = author;
        this.publicationYear = publicationYear;
        this.price = price;
    }
}