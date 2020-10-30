package com.bookshop.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.Length;


/**
 * Order Item Entity
 *
 */
@Entity
@Table(name = "ORDER_ITEM")
public class OrderItem {
	
	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(name = "d", updatable = false, nullable = false,unique = true)
	private String id;

	@NotBlank
	@Length(max = 36)
	@Column(name = "PRODUCT_ID", nullable = false, length = 36) 
	private String productId;

	@NotBlank
	@Length(max = 50)
	@Column(name = "PRODUCT_NAME", nullable = false, length = 36) 
	private String productName;

	@NotNull
	@Min(value = 0)
	@Column(name = "PRICE", nullable = false) 
	private BigDecimal price;

	@NotNull
	@Column(name = "COUNT", nullable = false) 
	private Integer count;
	
	@ManyToOne
    @JoinColumn(name="ORDER_ID", nullable=false)
    private Order order;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	
}
