package com.bookshop.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.bookshop.business.BookService;
import com.bookshop.entity.Book;
import com.bookshop.repository.BookRepository;
import com.bookshop.to.BookPutTO;
import com.bookshop.to.BookTO;

@ExtendWith(SpringExtension.class)
class BookServiceTest {

	@Mock
	private BookRepository repository; 
	
	@InjectMocks
	private BookService service;
	
	@BeforeEach
	void setUp() throws Exception {
		
	}

	@Test
	@DisplayName("read book, expected ok")
	void testRead() {
		String id = UUID.randomUUID().toString();
		BigDecimal price = new BigDecimal("12.2");
		String name ="The book";
		when(repository.findByIdAndDeletionTimestampNull(id)).thenReturn(buildBook(id, price, name) );
		BookTO erg = service.read(id);
		assertEquals(id, erg.getId());
		assertEquals(name, erg.getName());
		assertEquals(price, erg.getPrice());
	}

	@Test
	@DisplayName("read unknown book, expected NoSuchElementException")
	void testUnknownRead() {
		String id = UUID.randomUUID().toString();
		when(repository.findByIdAndDeletionTimestampNull(id)).thenReturn(null );
		Assertions.assertThrows(NoSuchElementException.class, () -> {
			service.read(id);
		  });
		
	}
	@Test
	@DisplayName("insert a book, expected ok")
	void testInsert() {
		BigDecimal price = new BigDecimal("12.2");
		String name ="The book";
		BookPutTO book = new BookPutTO();
		String uuid = UUID.randomUUID().toString();
		book.setName(name);
		book.setPrice(price);
		when(repository.save(Mockito.any())).thenReturn(buildBook(uuid, price, name) );
		
		service.insert(book);
		
		ArgumentCaptor<Book> capture = ArgumentCaptor.forClass(Book.class);
		verify(repository, times(1)).save(capture.capture());
		Book entity = capture.getValue();
		assertEquals(name, entity.getName());
		assertEquals(price, entity.getPrice());
		assertNull(entity.getDeletionTimestamp());
		
	}

	@Test
	@DisplayName("update a book, expected ok")
	void testUpdate() {
		BigDecimal price = new BigDecimal("12.2");
		String name ="The book";
		BookPutTO book = new BookPutTO();
		String uuid = UUID.randomUUID().toString();
		book.setName(name);
		book.setPrice(price);
		
		Book persistedBook = buildBook(uuid, price, name);
		when(repository.save(Mockito.any())).thenReturn(persistedBook );
		when(repository.findByIdAndDeletionTimestampNull(uuid)).thenReturn(persistedBook  );
		
		service.update(uuid, book);
		
		ArgumentCaptor<Book> capture = ArgumentCaptor.forClass(Book.class);
		verify(repository, times(1)).save(capture.capture());
		Book entity = capture.getValue();
		assertEquals(name, entity.getName());
		assertEquals(price, entity.getPrice());
		assertNull(entity.getDeletionTimestamp());
	}

	@Test
	@DisplayName("update a unknown book, expected NoSuchElementException")
	void testUnknownUpdate() {
		BigDecimal price = new BigDecimal("12.2");
		String name ="The book";
		BookPutTO book = new BookPutTO();
		book.setName(name);
		book.setPrice(price);
		String id = UUID.randomUUID().toString();
		when(repository.findByIdAndDeletionTimestampNull(id)).thenReturn(null );
		Assertions.assertThrows(NoSuchElementException.class, () -> {
			service.update(id, book);
		  });
	}
	
	
	@Test
	@DisplayName("delete book, expected ok")
	void testDelete() {
		String id = UUID.randomUUID().toString();
		BigDecimal price = new BigDecimal("12.2");
		String name ="The book";
		when(repository.findByIdAndDeletionTimestampNull(id)).thenReturn(buildBook(id, price, name) );
		service.delete(id);
		ArgumentCaptor<Book> capture = ArgumentCaptor.forClass(Book.class);

		verify(repository, times(1)).save(capture.capture());
		Book entity = capture.getValue();
		assertEquals(name, entity.getName());
		assertEquals(price, entity.getPrice());
		assertNotNull(entity.getDeletionTimestamp());
	}

	@Test
	@DisplayName("delete unknown book, expected NoSuchElementException")
	void testUnknownDelete() {
		String id = UUID.randomUUID().toString();
		when(repository.findByIdAndDeletionTimestampNull(id)).thenReturn(null );
		Assertions.assertThrows(NoSuchElementException.class, () -> {
			service.delete(id);
		  });
		
	}

	@Test
	@DisplayName("Read all books")
	void testReadAll() {
		String id1 = UUID.randomUUID().toString();
		BigDecimal price1 = new BigDecimal("33.3");
		String name1 ="The book";

		String id2 = UUID.randomUUID().toString();
		BigDecimal price2 = new BigDecimal("12.2");
		String name2 ="The book2";

		List<Book> bookList = new ArrayList<>();
		bookList.add(buildBook(id1, price1, name1));
		bookList.add(buildBook(id2, price2, name2));
		when(repository.findByDeletionTimestampNull()).thenReturn(bookList );
		
		List<BookTO> erg = service.readAll();
		assertEquals(id1, erg.get(0).getId());
		assertEquals(name1, erg.get(0).getName());
		assertEquals(price1, erg.get(0).getPrice());

		assertEquals(id2, erg.get(1).getId());
		assertEquals(name2, erg.get(1).getName());
		assertEquals(price2, erg.get(1).getPrice());
	}

	private Book buildBook(String id, BigDecimal price, String name) {
		Book bookEntity = new Book();
		bookEntity.setId(id);
		bookEntity.setName(name);
		bookEntity.setPrice(price);
		return bookEntity;
	}


}
