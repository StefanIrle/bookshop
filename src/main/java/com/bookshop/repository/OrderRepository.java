package com.bookshop.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.bookshop.entity.Order;

@Repository
public interface OrderRepository extends CrudRepository<Order, String>{

	List<Order> findByCreateTimestampAfterAndCreateTimestampBefore(LocalDateTime fromDateTime,
			LocalDateTime toDateTime);

}
