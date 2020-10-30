package com.bookshop.business;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.bookshop.entity.Order;
import com.bookshop.entity.OrderItem;
import com.bookshop.repository.OrderRepository;
import com.bookshop.to.BookTO;
import com.bookshop.to.OrderItemPutTO;
import com.bookshop.to.OrderItemTO;
import com.bookshop.to.OrderPutTO;
import com.bookshop.to.OrderTO;

/**
 * Service to place and retrieve orders
 *
 */
@Validated
@Service
@Transactional
public class OrderService {
	
	@Autowired
	private OrderRepository orderRepository; 


	@Autowired
	private BookService bookService;
	/**
	 * Insert a order. Validate the input data.
	 * @param orderPutTO order
	 * @return Data of inserted order
	 */
	@Transactional(timeout = 3)
	public OrderTO insert(@Valid OrderPutTO orderPutTO) {
		Order orderEntity = new Order();
		orderEntity.setEmail(orderPutTO.getEmail());
		orderEntity.setCreateTimestamp(LocalDateTime.now());
		List<OrderItem> items = buildOrderItemList(orderPutTO.getProducts(), orderEntity);
		orderEntity.setItems(items );
		Order persistedEntity = orderRepository.save(orderEntity);
		return mapToOrderTO(persistedEntity);
	}

	/**
	 * Return a list of all orders in a given time period
	 * @return List of orders
	 */
	@Transactional(timeout = 3)
	public List<OrderTO> readAll(LocalDateTime fromDateTime, LocalDateTime toDateTime) {
		if (fromDateTime == null) {
			fromDateTime = LocalDateTime.of(1900, 1, 1, 0, 0, 0, 0);
		}

		if (toDateTime == null) {
			toDateTime = LocalDateTime.of(2200, 1, 1, 0, 0, 0, 0);
		}

		return orderRepository.findByCreateTimestampAfterAndCreateTimestampBefore(fromDateTime, toDateTime).stream().map(OrderService::mapToOrderTO).collect(Collectors.toList());
	}
	
	private static OrderTO mapToOrderTO(Order orderEntity) {
		OrderTO orderTO = new OrderTO();
		orderTO.setEmail(orderEntity.getEmail());
		orderTO.setCreateTimestamp(orderEntity.getCreateTimestamp());
		orderTO.setId(orderEntity.getId());
		orderTO.setProducts(mapToOrderItemPutTO(orderEntity.getItems()));
		return orderTO;
	}
	
	private static List<OrderItemTO> mapToOrderItemPutTO(List<OrderItem> items) {
		return items.stream().map(OrderItemTO::new).collect(Collectors.toList());
	}

	
	private List<OrderItem> buildOrderItemList(List<OrderItemPutTO> products, Order orderEntity) {
		List<OrderItem> itemList = new ArrayList<>();
		for (OrderItemPutTO product: products) {
			OrderItem item = new OrderItem();
			item.setCount(product.getCount());
			BookTO book = bookService.read(product.getProductId());
			item.setProductId(product.getProductId());
			item.setPrice(book.getPrice());
			item.setProductName(book.getName());
			item.setOrder(orderEntity);
			itemList.add(item);
		}
		return itemList;
	}

	
}
