package com.dammy.bookstoreapi;

import com.dammy.bookstoreapi.model.Book;
import com.dammy.bookstoreapi.model.User;
import com.dammy.bookstoreapi.repository.BookRepository;
import com.dammy.bookstoreapi.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public DataSeeder(UserRepository userRepository, BookRepository bookRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {

        // Seeding for 'author1'
        if (userRepository.findByUsername("author1").isEmpty()) {
            User user = new User();
            user.setUsername("author1");
            user.setPassword(passwordEncoder.encode("password"));
            user.setName("Author 1");
            user.setRole("USER"); // Assign a role for user
            userRepository.save(user);
        }

        // Seeding for 'admin'
        if (userRepository.findByUsername("admin").isEmpty()) {
            User adminUser = new User();
            adminUser.setUsername("admin");
            adminUser.setPassword(passwordEncoder.encode("adminpass"));
            adminUser.setName("Administrator");
            adminUser.setRole("ADMIN"); // Assign the admin role
            userRepository.save(adminUser);
        }

        // Seeding a book for 'author1'
        if (bookRepository.findByIsbn("123-4567890123") == null) {
            User author = userRepository.findByUsername("author1")
                    .orElseThrow(() -> new RuntimeException("User 'author1' not found"));
            Book book = new Book();
            book.setTitle("The Great Mystery");
            book.setGenre("Mystery");
            book.setIsbn("123-4567890123");
            book.setAuthor(author);
            book.setPublicationYear(2021);
            book.setPrice(45.0);
            bookRepository.save(book);
        }
    }

}
