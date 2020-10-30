package com.bookshop.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
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
import com.bookshop.business.OrderService;
import com.bookshop.entity.Order;
import com.bookshop.entity.OrderItem;
import com.bookshop.repository.OrderRepository;
import com.bookshop.to.BookTO;
import com.bookshop.to.OrderItemPutTO;
import com.bookshop.to.OrderItemTO;
import com.bookshop.to.OrderPutTO;
import com.bookshop.to.OrderTO;

@ExtendWith(SpringExtension.class)
class OrderServiceTest {

	@Mock
	private OrderRepository repository; 
	
	@Mock 
	private BookService bookService;
	
	@InjectMocks
	private OrderService service;
	
	@BeforeEach
	void setUp() throws Exception {
		
	}
	@Test
	@DisplayName("Insert a order, expected ok")
	void testInsert() {
		String email = "test@test.de";
		String uuid = UUID.randomUUID().toString();

		when(bookService.read(uuid)).thenReturn(buildBook(uuid) );
		String productName = "Java";
		Integer count = 2;
		BigDecimal price = new BigDecimal("20.2");
		Order orderentity = createOrderEntity(email, productName, count, price, uuid, UUID.randomUUID().toString());
		when(repository.save(Mockito.any())).thenReturn(orderentity);
		
		OrderPutTO order = new OrderPutTO();
		order.setEmail(email);
		List<OrderItemPutTO> products = new ArrayList<>();
		OrderItemPutTO item= new OrderItemPutTO();
		item.setCount(3);
		item.setProductId(uuid);
		products.add(item);
		order.setProducts(products);
		
		service.insert(order);
		
		ArgumentCaptor<Order> capture = ArgumentCaptor.forClass(Order.class);
		verify(repository, times(1)).save(capture.capture());
		Order entity = capture.getValue();
		assertEquals(email, entity.getEmail());
		assertEquals(1, entity.getItems().size());
	}


	@Test
	@DisplayName("Read all orders")
	void testReadAllOrders() {
		String email = "test@test.de";
		String productName = "Java";
		Integer count = 2;
		BigDecimal price = new BigDecimal("20.2");
		String productId = UUID.randomUUID().toString();
		String uuid = UUID.randomUUID().toString();
		LocalDateTime fromTime = LocalDateTime.of(1900, 1, 1, 0, 0, 0, 0);
		LocalDateTime toTime = LocalDateTime.of(2200, 1, 1, 0, 0, 0, 0);
		List<Order> orderList = new ArrayList<>();
		Order order = createOrderEntity(email, productName, count, price, productId, uuid);
		orderList.add(order);
		when(repository.findByCreateTimestampAfterAndCreateTimestampBefore(fromTime, toTime)).thenReturn(orderList );
		List<OrderTO> result = service.readAll(fromTime,toTime);
		assertEquals(1, result.size());
		OrderTO resultOrder = result.get(0);
		assertEquals(email, resultOrder.getEmail());
		assertEquals(new BigDecimal("40.4"),resultOrder.getTotalOrderAmount());
		assertEquals(uuid, resultOrder.getId());
		assertEquals(1, resultOrder.getProducts().size());
		OrderItemTO resultItem = resultOrder.getProducts().get(0);
		assertEquals(count, resultItem.getCount());
		assertEquals(price, resultItem.getPrice());
		assertEquals(productId, resultItem.getProductId());
		assertEquals(productName, resultItem.getProductName());
	}

	@Test
	@DisplayName("Read all orders with missing timestamps")
	void testReadAllOrdersWithoutTimestamps() {
		LocalDateTime fromTime = LocalDateTime.of(1900, 1, 1, 0, 0, 0, 0);
		LocalDateTime toTime = LocalDateTime.of(2200, 1, 1, 0, 0, 0, 0);
		List<Order> orderList = new ArrayList<>();
		when(repository.findByCreateTimestampAfterAndCreateTimestampBefore(null, null)).thenReturn(orderList  );
		service.readAll(null,null);
		verify(repository, times(1)).findByCreateTimestampAfterAndCreateTimestampBefore(fromTime, toTime);
		
	}

	private Order createOrderEntity(String email, String productName, Integer count, BigDecimal price, String productId,
			String uuid) {
		Order order = new Order();
		order.setCreateTimestamp(LocalDateTime.now());
		order.setEmail(email);
		order.setId(uuid);
		List<OrderItem> items = new ArrayList<>();
		OrderItem item = new OrderItem();
		item.setCount(count);
		item.setOrder(order);
		item.setId(UUID.randomUUID().toString());
		item.setProductId(productId);
		item.setProductName(productName);
		item.setPrice(price);
		items.add(item);
		order.setItems(items);
		return order;
	}
	
	private BookTO buildBook(String uuid) {
		BookTO book = new BookTO();
		book.setId(uuid);
		book.setCreateTimestamp(LocalDateTime.now());
		book.setName("Java");
		book.setPrice(new BigDecimal(20.2));
		return book;
	}

}
