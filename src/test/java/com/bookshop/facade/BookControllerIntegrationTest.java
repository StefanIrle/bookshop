package com.bookshop.facade;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.bookshop.entity.Book;
import com.bookshop.repository.BookRepository;
import com.bookshop.to.BookPutTO;
import com.bookshop.to.BookTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class BookControllerIntegrationTest {
	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private BookRepository repository;

	@Test
	@DisplayName("Insert book")
	public void insertBook() throws Exception {
		String name = "Java";
		BigDecimal price = new BigDecimal(20.2);
		String uuid = UUID.randomUUID().toString();

		BookPutTO book = new BookPutTO();
		book.setName(name);
		book.setPrice(price);

		final ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.findAndRegisterModules();
		final String json = objectMapper.writeValueAsString(book);

		when(repository.save(Mockito.any())).thenReturn(buildBook(name, price, uuid));
		
		ResultActions restServiceResult = this.mockMvc.perform(put("/books").content(json)
				.accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk());
		String content = restServiceResult.andReturn().getResponse().getContentAsString();
		BookTO result = objectMapper.readValue(content, BookTO.class);
		assertEquals(name, result.getName());
		assertEquals(price, result.getPrice());
		assertEquals(uuid, result.getId());

		ArgumentCaptor<Book> capture = ArgumentCaptor.forClass(Book.class);
		verify(repository, times(1)).save(capture.capture());
		Book entity = capture.getValue();
		assertEquals(name, entity.getName());
		assertEquals(price, entity.getPrice());
	}

	@Test
	@DisplayName("Insert book with invalid data")
	public void insertBookValidationError() throws Exception {
		BookPutTO book = new BookPutTO();
		book.setName(null);
		book.setPrice(null);

		final ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.findAndRegisterModules();
		final String json = objectMapper.writeValueAsString(book);

		this.mockMvc.perform(put("/books/").content(json).accept(MediaType.APPLICATION_JSON_VALUE)
				.contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isBadRequest());
	}

	@Test
	@DisplayName("Update book")
	public void updateBook() throws Exception {
		String name = "Java";
		BigDecimal price = new BigDecimal(20.2);

		BookPutTO book = new BookPutTO();
		book.setName(name);
		book.setPrice(price);
		
		String uuid = UUID.randomUUID().toString();
		
		Book bookEntity = new Book();
		bookEntity.setId(uuid);
		bookEntity.setCreateTimestamp(LocalDateTime.now());
		bookEntity.setName("Java2");
		bookEntity.setPrice(price);
		
		when(repository.findByIdAndDeletionTimestampNull(uuid)).thenReturn(bookEntity );

		final ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.findAndRegisterModules();
		final String json = objectMapper.writeValueAsString(book);

		ResultActions restServiceResult = this.mockMvc.perform(put("/books/"+uuid).content(json)
				.accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk());
		String content = restServiceResult.andReturn().getResponse().getContentAsString();
		BookTO result = objectMapper.readValue(content, BookTO.class);
		assertEquals(name, result.getName());
		assertEquals(price, result.getPrice());

		ArgumentCaptor<Book> capture = ArgumentCaptor.forClass(Book.class);

		verify(repository, times(1)).save(capture.capture());
		Book entity = capture.getValue();
		assertEquals(name, entity.getName());
		assertEquals(price, entity.getPrice());

		assertEquals(result.getId(), entity.getId());
	}

	@Test
	@DisplayName("Update book with invalid data")
	public void updateBookValidation() throws Exception {
		BookPutTO book = new BookPutTO();
		book.setName(null);
		book.setPrice(null);

		final ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.findAndRegisterModules();
		final String json = objectMapper.writeValueAsString(book);
		String uuid = UUID.randomUUID().toString();

		this.mockMvc.perform(put("/books/"+uuid).content(json).accept(MediaType.APPLICATION_JSON_VALUE)
				.contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isBadRequest());
	}


	@Test
	@DisplayName("Read book")
	public void readBook() throws Exception {
		String name = "Java";
		BigDecimal price = new BigDecimal(20.2);

		String uuid = UUID.randomUUID().toString();
		
		Book bookEntity = buildBook(name, price, uuid);
		when(repository.findByIdAndDeletionTimestampNull(uuid)).thenReturn(bookEntity );

		final ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.findAndRegisterModules();

		ResultActions restServiceResult = this.mockMvc.perform(get("/books/"+uuid)
				.accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk());
		String content = restServiceResult.andReturn().getResponse().getContentAsString();
		BookTO result = objectMapper.readValue(content, BookTO.class);
		assertEquals(name, result.getName());
		assertEquals(price, result.getPrice());
	}

	@Test
	@DisplayName("delete book")
	public void deleteBook() throws Exception {
		String uuid = UUID.randomUUID().toString();
		String name = "Java";
		BigDecimal price = new BigDecimal(20.2);

		
		Book bookEntity = buildBook(name, price, uuid);
		when(repository.findByIdAndDeletionTimestampNull(uuid)).thenReturn(bookEntity );

		
		this.mockMvc.perform(delete("/books/"+uuid)
				.accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isNoContent());

		ArgumentCaptor<Book> capture = ArgumentCaptor.forClass(Book.class);

		verify(repository, times(1)).save(capture.capture());
		assertEquals(uuid,capture.getValue().getId());
		assertNotNull(capture.getValue().getDeletionTimestamp());
		
	}

	@Test
	@DisplayName("Read all books")
	public void readAllBooks() throws Exception {
		String name = "Java";
		String name2 = "Java";
		BigDecimal price = new BigDecimal(20.2);
		BigDecimal price2 = new BigDecimal(30.3);

		String uuid = UUID.randomUUID().toString();
		String uuid2 = UUID.randomUUID().toString();
		
		Book bookEntity = buildBook(name, price, uuid);
		Book bookEntity2 = buildBook(name2, price2, uuid2);

		
		List<Book> bookList = new ArrayList<>();
		bookList.add(bookEntity);
		bookList.add(bookEntity2);
		
		when(repository.findByDeletionTimestampNull()).thenReturn(bookList  );

		final ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.findAndRegisterModules();

		ResultActions restServiceResult = this.mockMvc.perform(get("/books")
				.accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk());
		String content = restServiceResult.andReturn().getResponse().getContentAsString();
		List<BookTO> list = objectMapper.readValue(content, new TypeReference<List<BookTO>>(){});
		
		assertEquals(2, list.size());
		assertEquals(name, list.get(0).getName());
		assertEquals(price, list.get(0).getPrice());
		assertEquals(uuid, list.get(0).getId());

		assertEquals(name2, list.get(1).getName());
		assertEquals(price2, list.get(1).getPrice());
		assertEquals(uuid2, list.get(1).getId());
	}

	private Book buildBook(String name, BigDecimal price, String uuid) {
		Book bookEntity = new Book();
		bookEntity.setId(uuid);
		bookEntity.setCreateTimestamp(LocalDateTime.now());
		bookEntity.setName(name);
		bookEntity.setPrice(price);
		return bookEntity;
	}

}
