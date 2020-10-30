package com.bookshop.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.bookshop.entity.Book;

@Repository
public interface BookRepository extends CrudRepository<Book, String>{

	Book findByIdAndDeletionTimestampNull(String id);

	List<Book> findByDeletionTimestampNull();
	
}
