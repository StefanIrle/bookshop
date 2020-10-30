package com.bookshop.facade;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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

import com.bookshop.business.BookService;
import com.bookshop.entity.Order;
import com.bookshop.entity.OrderItem;
import com.bookshop.repository.OrderRepository;
import com.bookshop.to.BookTO;
import com.bookshop.to.OrderItemPutTO;
import com.bookshop.to.OrderItemTO;
import com.bookshop.to.OrderPutTO;
import com.bookshop.to.OrderSearchTO;
import com.bookshop.to.OrderTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class OrderControllerIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private OrderRepository repository;

	@MockBean
	private BookService bookService;

	@Test
	@DisplayName("Insert order")
	public void insertBook() throws Exception {
		String email = "test@test.de";
		String uuid = UUID.randomUUID().toString();
		OrderPutTO orderPutTO = new OrderPutTO();
		orderPutTO.setEmail(email);
		List<OrderItemPutTO> products = new ArrayList<>();
		OrderItemPutTO item= new OrderItemPutTO();
		item.setCount(3);
		item.setProductId(uuid);
		products.add(item);
		orderPutTO.setProducts(products);
		
		when(bookService.read(uuid)).thenReturn(buildBook(uuid) );
		
		final ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.findAndRegisterModules();
		final String json = objectMapper.writeValueAsString(orderPutTO);

		String productName = "Java";
		Integer count = 2;
		BigDecimal price = new BigDecimal(11.2);
		String productId = UUID.randomUUID().toString();
		when(repository.save(Mockito.any())).thenReturn(
				createOrderEntity(email, productName, count, price, productId, uuid));
		
		ResultActions restServiceResult = this.mockMvc.perform(put("/orders").content(json)
				.accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk());
		String content = restServiceResult.andReturn().getResponse().getContentAsString();
		OrderTO result = objectMapper.readValue(content, OrderTO.class);
		assertEquals(email, result.getEmail());
		assertEquals(1, result.getProducts().size());
		assertEquals(new BigDecimal(22.4).longValue(), result.getTotalOrderAmount().longValue());

		ArgumentCaptor<Order> capture = ArgumentCaptor.forClass(Order.class);
		verify(repository, times(1)).save(capture.capture());
		Order entity = capture.getValue();
		assertEquals(email, entity.getEmail());
	}

	@Test
	@DisplayName("Read all orders")
	public void readAllOrders() throws Exception {
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
		when(repository.findByCreateTimestampAfterAndCreateTimestampBefore(fromTime, toTime)).thenReturn(orderList);

		OrderSearchTO searchTO = new OrderSearchTO();
		searchTO.setFrom(fromTime);
		searchTO.setTo(toTime);
		final ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.findAndRegisterModules();
		final String json = objectMapper.writeValueAsString(searchTO);

		ResultActions restServiceResult = this.mockMvc.perform(post("/orders").content(json)
				.accept(MediaType.APPLICATION_JSON_VALUE).contentType(MediaType.APPLICATION_JSON_VALUE))
				.andExpect(status().isOk());
		String content = restServiceResult.andReturn().getResponse().getContentAsString();
		List<OrderTO> list = objectMapper.readValue(content, new TypeReference<List<OrderTO>>(){});
		
		assertEquals(1, list.size());
		OrderTO resultOrder = list.get(0);
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
