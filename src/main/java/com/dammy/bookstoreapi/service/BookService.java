package com.dammy.bookstoreapi.service;

import com.dammy.bookstoreapi.dto.BookDTO;
import com.dammy.bookstoreapi.dto.UserDTO;
import com.dammy.bookstoreapi.model.Book;
import com.dammy.bookstoreapi.model.User;
import com.dammy.bookstoreapi.repository.BookRepository;
import com.dammy.bookstoreapi.repository.UserRepository;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.security.access.AccessDeniedException;

import jakarta.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookService {
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    public BookService(BookRepository bookRepository, UserRepository userRepository) {
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    public List<BookDTO> findAll() {
        return bookRepository.findAll().stream()
                .map(book -> new BookDTO(
                        book.getId(),
                        book.getTitle(),
                        book.getGenre(),
                        book.getIsbn(),
                        convertToUserDTO(book.getAuthor()),
                        book.getPublicationYear(),
                        book.getPrice()
                ))
                .collect(Collectors.toList());
    }

    private UserDTO convertToUserDTO(User user) {
        if (user == null) {
            return null;
        }
        return new UserDTO(
                user.getId(),
                user.getUsername(),
                user.getName(),
                user.getRole()
        );
    }

    private String getAuthenticatedUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("User is not authenticated");
        }

        return authentication.getName();
    }

    public BookDTO save(Book book) {
        if (book == null) {
            throw new IllegalArgumentException("Book cannot be null");
        }

        if (book.getTitle() == null || book.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Book title cannot be empty");
        }

        // Get the authenticated user
        String username = getAuthenticatedUsername();

        User author = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        book.setAuthor(author);

        // Save the book
        Book savedBook = bookRepository.save(book);

        // Return a DTO to exclude password
        return new BookDTO(
                savedBook.getId(),
                savedBook.getTitle(),
                savedBook.getGenre(),
                savedBook.getIsbn(),
                convertToUserDTO(book.getAuthor()),
                savedBook.getPublicationYear(),
                book.getPrice()
        );
    }

    public Optional<Book> findById(Long id) {
        return bookRepository.findById(id);
    }

    public List<BookDTO> searchBooks(String title, String author, Integer year, String genre) {
        // Create a Specification for dynamic querying
        Specification<Book> specification = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (title != null) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), "%" + title.toLowerCase() + "%"));
            }
            if (author != null) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("author").get("name")), "%" + author.toLowerCase() + "%"));
            }
            if (year != null) {
                predicates.add(criteriaBuilder.equal(root.get("publicationYear"), year));
            }
            if (genre != null) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("genre")), "%" + genre.toLowerCase() + "%"));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        // Execute the query
        List<Book> books = bookRepository.findAll(specification);

        // Map the results to BookDTO
        return books.stream()
                .map(book -> new BookDTO(
                        book.getId(),
                        book.getTitle(),
                        book.getGenre(),
                        book.getIsbn(),
                        convertToUserDTO(book.getAuthor()),
                        book.getPublicationYear(),
                        book.getPrice()
                ))
                .collect(Collectors.toList());
    }
}
