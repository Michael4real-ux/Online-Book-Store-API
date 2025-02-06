package com.dammy.bookstoreapi;

import com.dammy.bookstoreapi.dto.BookDTO;
import com.dammy.bookstoreapi.model.Book;
import com.dammy.bookstoreapi.model.User;
import com.dammy.bookstoreapi.repository.BookRepository;
import com.dammy.bookstoreapi.repository.UserRepository;
import com.dammy.bookstoreapi.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class BookServiceTest {

	@InjectMocks
	private BookService bookService;

	@Mock
	private BookRepository bookRepository;

	@Mock
	private UserRepository userRepository;

	@BeforeEach
	public void init() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void testFindAll() {
		User author1 = new User();
		author1.setId(1L);
		author1.setName("Author 1");

		User author2 = new User();
		author2.setId(2L);
		author2.setName("Author 2");

		Book book1 = new Book();
		book1.setTitle("Book 1");
		book1.setAuthor(author1);

		Book book2 = new Book();
		book2.setTitle("Book 2");
		book2.setAuthor(author2);

		when(bookRepository.findAll()).thenReturn(List.of(book1, book2));

		List<BookDTO> books = bookService.findAll();
		assertEquals(2, books.size());
		assertEquals("Book 1", books.get(0).title());
		assertEquals("Author 1", books.get(0).author().getName());
		assertEquals("Book 2", books.get(1).title());
		assertEquals("Author 2", books.get(1).author().getName());
	}

	@Test
	public void testSave() {
		User author = new User();
		author.setId(1L);
		author.setName("Author 1");
		author.setUsername("testUser");

		Book book = new Book();
		book.setTitle("Book 1");
		book.setAuthor(author);
		book.setPublicationYear(2022);

		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Authentication authentication = Mockito.mock(Authentication.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		Mockito.when(authentication.isAuthenticated()).thenReturn(true);
		Mockito.when(authentication.getName()).thenReturn("testUser");

		try (MockedStatic<SecurityContextHolder> mockedSecurityContextHolder = Mockito.mockStatic(SecurityContextHolder.class)) {
			mockedSecurityContextHolder.when(SecurityContextHolder::getContext).thenReturn(securityContext);

			when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(author));
			when(bookRepository.save(any(Book.class))).thenReturn(book);

			BookDTO savedBookDTO = bookService.save(book);

			assertEquals("Book 1", savedBookDTO.title());
			assertEquals("Author 1", savedBookDTO.author().getName());
			assertEquals(2022, savedBookDTO.publicationYear());

			verify(userRepository, times(1)).findByUsername("testUser");
			verify(bookRepository, times(1)).save(any(Book.class));
		}
	}

	@Test
	public void testFindById() {
		User author = new User();
		author.setId(1L);
		author.setName("Author 1");

		Book book = new Book();
		book.setId(1L);
		book.setTitle("Book 1");
		book.setAuthor(author);

		when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

		Optional<Book> foundBook = bookService.findById(1L);
		assertTrue(foundBook.isPresent());
		assertEquals("Book 1", foundBook.get().getTitle());
	}

	@Test
	public void testFindByIdNotFound() {
		when(bookRepository.findById(anyLong())).thenReturn(Optional.empty());

		Optional<Book> foundBook = bookService.findById(2L);
		assertFalse(foundBook.isPresent());
	}

	@Test
	public void testSearchBooksByTitle() {
		Book book = new Book();
		book.setTitle("Test Book");
		book.setPublicationYear(2023);

		when(bookRepository.findAll(any(Specification.class))).thenReturn(List.of(book));

		List<BookDTO> books = bookService.searchBooks("Test", null, null, null);
		assertEquals(1, books.size()); // Ensure at least one book is found
		assertEquals("Test Book", books.get(0).title());
	}


	@Test
	public void testSearchBooksByAuthorName() {
		User author = new User();
		author.setId(1L);
		author.setName("Author 1");

		Book book = new Book();
		book.setTitle("Book 1");
		book.setAuthor(author);

		when(bookRepository.findAll(any(Specification.class))).thenReturn(List.of(book));

		List<BookDTO> result = bookService.searchBooks(null, "Author 1", null, null);
		assertEquals(1, result.size());
		assertEquals("Author 1", result.get(0).author().getName());
	}

	@Test
	public void testSearchBooksByYear() {
		User author = new User();
		author.setId(1L);
		author.setName("Author 1");

		Book book = new Book();
		book.setTitle("Book 1");
		book.setAuthor(author);
		book.setPublicationYear(2022);

		when(bookRepository.findAll(any(Specification.class))).thenReturn(List.of(book));

		List<BookDTO> result = bookService.searchBooks(null, null, 2022, null);
		assertEquals(1, result.size());
		assertEquals("Book 1", result.get(0).title());
		assertEquals("Author 1", result.get(0).author().getName());
		assertEquals(2022, result.get(0).publicationYear());
	}


	@Test
	public void testSearchBooksByMultipleCriteria() {
		User author = new User();
		author.setId(1L);
		author.setName("Author 1");

		Book book = new Book();
		book.setTitle("Book 1");
		book.setAuthor(author);
		book.setPublicationYear(2022);
		book.setGenre("Fiction");

		when(bookRepository.findAll(any(Specification.class))).thenReturn(List.of(book));

		List<BookDTO> result = bookService.searchBooks("Book 1", "Author 1", 2022, "Fiction");

		assertNotNull(result);
		assertEquals(1, result.size());

		BookDTO bookDTO = result.get(0);
		assertEquals("Book 1", bookDTO.title());
		assertNotNull(bookDTO.author());
		assertEquals("Author 1", bookDTO.author().getName());
		assertEquals(2022, bookDTO.publicationYear());
		assertEquals("Fiction", bookDTO.genre());
	}

	@Test
	public void testSearchBooksNoResults() {
		when(bookRepository.findByTitleContainingIgnoreCase(anyString())).thenReturn(new ArrayList<>());

		List<BookDTO> result = bookService.searchBooks("Non-existent book", null, null, null);
		assertTrue(result.isEmpty());
	}

	@Test
	public void testSearchBooksRepositoryException() {
		when(bookRepository.findAll(any(Specification.class))).thenThrow(new RuntimeException("Database error"));

		assertThrows(RuntimeException.class, () -> {
			bookService.searchBooks(null, null, null, null);
		});
	}

}
