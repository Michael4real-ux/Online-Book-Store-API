package com.dammy.bookstoreapi;

import com.dammy.bookstoreapi.model.Book;
import com.dammy.bookstoreapi.repository.BookRepository;
import com.dammy.bookstoreapi.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

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

	@BeforeEach
	public void init() {
		MockitoAnnotations.openMocks(this);
	}

	@Test
	public void testFindAll() {
		Book book1 = new Book();
		book1.setTitle("Book 1");
		book1.setAuthor("Author 1");

		Book book2 = new Book();
		book2.setTitle("Book 2");
		book2.setAuthor("Author 2");

		when(bookRepository.findAll()).thenReturn(List.of(book1, book2));

		List<Book> books = bookService.findAll();
		assertEquals(2, books.size());
		assertEquals("Book 1", books.get(0).getTitle());
		assertEquals("Book 2", books.get(1).getTitle());
	}

	@Test
	public void testSave() {
		Book book = new Book();
		book.setTitle("Book 1");
		book.setAuthor("Author 1");

		when(bookRepository.save(any(Book.class))).thenReturn(book);

		Book savedBook = bookService.save(book);
		assertEquals("Book 1", savedBook.getTitle());
		verify(bookRepository, times(1)).save(book);
	}

	@Test
	public void testSaveNullBook() {
		Exception exception = assertThrows(IllegalArgumentException.class, () -> {
			bookService.save(null);
		});

		String expectedMessage = "Book cannot be null";
		String actualMessage = exception.getMessage();

		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	public void testSaveBookWithMissingFields() {
		Book book = new Book();
		book.setAuthor("Author 1"); // Missing title

		Exception exception = assertThrows(IllegalArgumentException.class, () -> {
			bookService.save(book);
		});

		String expectedMessage = "Book title cannot be empty";
		String actualMessage = exception.getMessage();

		assertTrue(actualMessage.contains(expectedMessage));
	}

	@Test
	public void testFindById() {
		Book book = new Book();
		book.setId(1L);
		book.setTitle("Book 1");
		book.setAuthor("Author 1");

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
	public void testSaveThrowsException() {
		Book book = new Book();
		book.setTitle("Book 1");
		book.setAuthor("Author 1");

		when(bookRepository.save(any(Book.class))).thenThrow(new RuntimeException("Database error"));

		Exception exception = assertThrows(RuntimeException.class, () -> {
			bookService.save(book);
		});

		assertEquals("Database error", exception.getMessage());
	}

	@Test
	public void testSearchBooksByTitle() {
		Book book = new Book();
		book.setTitle("Book 1");
		book.setAuthor("Author 1");

		when(bookRepository.findByTitleContainingIgnoreCase("Book 1")).thenReturn(List.of(book));

		List<Book> result = bookService.searchBooks("Book 1", null, null, null);
		assertEquals(1, result.size());
		assertEquals("Book 1", result.get(0).getTitle());
	}

	@Test
	public void testSearchBooksByAuthor() {
		Book book = new Book();
		book.setTitle("Book 1");
		book.setAuthor("Author 1");

		when(bookRepository.findByAuthorContainingIgnoreCase("Author 1")).thenReturn(List.of(book));

		List<Book> result = bookService.searchBooks(null, "Author 1", null, null);
		assertEquals(1, result.size());
		assertEquals("Author 1", result.get(0).getAuthor());
	}

	@Test
	public void testSearchBooksByYear() {
		Book book = new Book();
		book.setTitle("Book 1");
		book.setAuthor("Author 1");
		book.setPublicationYear(2022);

		when(bookRepository.findByPublicationYear(2022)).thenReturn(List.of(book));

		List<Book> result = bookService.searchBooks(null, null, 2022, null);
		assertEquals(1, result.size());
		assertEquals(2022, (long) result.get(0).getPublicationYear());
	}

	@Test
	public void testSearchBooksByGenre() {
		Book book = new Book();
		book.setTitle("Book 1");
		book.setAuthor("Author 1");
		book.setGenre("Fiction");

		when(bookRepository.findByGenreContainingIgnoreCase("Fiction")).thenReturn(List.of(book));

		List<Book> result = bookService.searchBooks(null, null, null, "Fiction");
		assertEquals(1, result.size());
		assertEquals("Fiction", result.get(0).getGenre());
	}

	@Test
	public void testSearchBooksByMultipleCriteria() {
		Book book = new Book();
		book.setTitle("Book 1");
		book.setAuthor("Author 1");
		book.setPublicationYear(2022);
		book.setGenre("Fiction");

		when(bookRepository.findByTitleContainingIgnoreCase("Book 1")).thenReturn(List.of(book));
		when(bookRepository.findByAuthorContainingIgnoreCase("Author 1")).thenReturn(List.of(book));
		when(bookRepository.findByPublicationYear(2022)).thenReturn(List.of(book));
		when(bookRepository.findByGenreContainingIgnoreCase("Fiction")).thenReturn(List.of(book));

		List<Book> result = bookService.searchBooks("Book 1", "Author 1", 2022, "Fiction");
		assertEquals(1, result.size());
		assertEquals("Book 1", result.get(0).getTitle());
		assertEquals("Author 1", result.get(0).getAuthor());
		assertEquals(2022, (long) result.get(0).getPublicationYear());
		assertEquals("Fiction", result.get(0).getGenre());
	}

	@Test
	public void testSearchBooksNoResults() {
		when(bookRepository.findByTitleContainingIgnoreCase(anyString())).thenReturn(new ArrayList<>());
		when(bookRepository.findByAuthorContainingIgnoreCase(anyString())).thenReturn(new ArrayList<>());
		when(bookRepository.findByPublicationYear(anyInt())).thenReturn(new ArrayList<>());
		when(bookRepository.findByGenreContainingIgnoreCase(anyString())).thenReturn(new ArrayList<>());

		List<Book> result = bookService.searchBooks("Non-existent book", "Non-existent author", 2022, "Non-existent genre");
		assertTrue(result.isEmpty());
	}

	@Test
	public void testSearchBooksRepositoryException() {
		when(bookRepository.findByTitleContainingIgnoreCase(anyString())).thenThrow(new RuntimeException("Repository error"));

		Exception exception = assertThrows(RuntimeException.class, () -> {
			bookService.searchBooks("Book 1", null, null, null);
		});

		assertEquals("Repository error", exception.getMessage());
	}
}