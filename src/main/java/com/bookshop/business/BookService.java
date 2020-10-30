package com.bookshop.business;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.bookshop.annotation.UUID;
import com.bookshop.entity.Book;
import com.bookshop.repository.BookRepository;
import com.bookshop.to.BookPutTO;
import com.bookshop.to.BookTO;

/**
 * CRUD functionality for Books
 *
 */
@Validated
@Service
@Transactional
public class BookService {
	
	@Autowired
	private BookRepository repository; 
	
	/**
	 * Read a (non deleted) book. Throws IllegalArgumentException id id is null and NoSuchElementException id element could not be found
	 * @param id id of the book
	 * @return book
	 */
	@Transactional(timeout = 3)
	public BookTO read(@UUID String id) {
		Book bookEntity = repository.findByIdAndDeletionTimestampNull(id);
		if (bookEntity == null) {
			throw buildNotFoundException(id);
		}
		return mapToBookTO(bookEntity);
	}


	/**
	 * Insert a Book. Validate the input data.
	 * @param bookPutTO book
	 * @return Data of inserted book
	 */
	@Transactional(timeout = 3)
	public BookTO insert(@Valid BookPutTO bookPutTO) {
		Book bookEntity = new Book();
		bookEntity.setName(bookPutTO.getName());
		bookEntity.setPrice(bookPutTO.getPrice());
		bookEntity.setCreateTimestamp(LocalDateTime.now());
		bookEntity.setDeletionTimestamp(null);
		Book persistedEntity = repository.save(bookEntity);
		return mapToBookTO(persistedEntity);
	}

	/**
	 * Update a (non deleted) book. Validate the input data. Throws NoSuchElementException if book could not be found
	 * @param id id of the book
	 * @param bookPutTO book
	 * @return data of updated book
	 */
	@Transactional(timeout = 3)
	public BookTO update(@UUID String id, @Valid BookPutTO bookPutTO) {
		Book bookEntity = repository.findByIdAndDeletionTimestampNull(id);
		if (bookEntity == null) {
			throw buildNotFoundException(id);
		}
		bookEntity.setName(bookPutTO.getName());
		bookEntity.setPrice(bookPutTO.getPrice());
		repository.save(bookEntity);
		return mapToBookTO(bookEntity);
	}

	/**
	 * Delete a book.
	 * @param id id of book
	 */
	@Transactional(timeout = 3)
	public void delete(@UUID String id) {
		Book bookEntity = repository.findByIdAndDeletionTimestampNull(id);
		if (bookEntity == null) {
			throw buildNotFoundException(id);
		}
		bookEntity.setDeletionTimestamp(LocalDateTime.now());
		repository.save(bookEntity);
	}
	
	/**
	 * Return a list of all (non deleted) books
	 * @return List of Books
	 */
	@Transactional(timeout = 3)
	public List<BookTO> readAll() {
		return repository.findByDeletionTimestampNull().stream().map(BookService::mapToBookTO).collect(Collectors.toList());
	}
	
	private static BookTO mapToBookTO(Book bookEntity) {
		BookTO bookTO = new BookTO();
		bookTO.setId(bookEntity.getId());
		bookTO.setName(bookEntity.getName());
		bookTO.setPrice(bookEntity.getPrice());
		bookTO.setCreateTimestamp(bookEntity.getCreateTimestamp());
		return bookTO;
	}

	private NoSuchElementException buildNotFoundException(String id) {
		return new NoSuchElementException("Book "+ id + " not found");
	}


}
