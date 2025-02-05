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

        if (userRepository.findByUsername("author1") == null) {
            User user = new User();
            user.setUsername("author1");
            user.setPassword(passwordEncoder.encode("password"));
            user.setName("Author 1");
            userRepository.save(user);
        }

        // Create the "admin" user (NEW!)
        if (userRepository.findByUsername("admin") == null) {
            User adminUser = new User();
            adminUser.setUsername("admin");
            adminUser.setPassword(passwordEncoder.encode("adminpass"));
            adminUser.setName("Administrator");
            userRepository.save(adminUser);
        }


        if (bookRepository.findByIsbn("123-4567890123") == null) {
            User author = userRepository.findByUsername("author1");
            Book book = new Book();
            book.setTitle("The Great Mystery");
            book.setGenre("Mystery");
            book.setIsbn("123-4567890123");
            book.setAuthor(author);
            book.setPublicationYear(2021);
            bookRepository.save(book);
        }
    }
}