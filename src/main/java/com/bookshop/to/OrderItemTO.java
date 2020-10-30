package com.bookshop.to;

import java.math.BigDecimal;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import com.bookshop.entity.OrderItem;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


@ApiModel(description = "Item of a order")
public class OrderItemTO {
	
	@NotBlank
	@Length(max = 36)
	@ApiModelProperty(notes = "The id of the item. (Id of book)", example = "a9bd6f23-cab1-4a1f-b235-a75dadc241aa", allowEmptyValue = false)
	private String productId;

	@NotBlank
	@Length(max = 50)
	@ApiModelProperty(notes = "The name of the item", example = "Java", allowEmptyValue = false)
	private String productName;
	
	@NotNull
	@ApiModelProperty(notes = "The price of the item", example = "42.00",allowEmptyValue = false)
	private BigDecimal price;

	@NotNull
	@ApiModelProperty(notes = "Count of items", example = "5",allowEmptyValue = false)
	private Integer count;

	public OrderItemTO() {
		price = BigDecimal.ZERO;
		count = 0;
	}

	public OrderItemTO(OrderItem item) {
		this.count = item.getCount();
		this.price = item.getPrice();
		this.productId = item.getProductId();
		this.productName = item.getProductName();
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


}
