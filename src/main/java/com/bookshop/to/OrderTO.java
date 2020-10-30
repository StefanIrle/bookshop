package com.bookshop.to;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


@ApiModel(description = "Information of a order")
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderTO {
	
	@NotBlank
	@ApiModelProperty(notes = "Unique ID", example = "712ccbab-0071-4700-9249-6cd4a709d3cb", allowEmptyValue = false)
	private String id;

	@ApiModelProperty(notes = "Time the order was placed", allowEmptyValue = false)
	private LocalDateTime createTimestamp;

	@Email
	@ApiModelProperty(notes = "Buyer’s e-mail", example = "stefan@irle.com", allowEmptyValue = false)
	private String email; 

	@NotNull
	@Size(min = 1)
	@ApiModelProperty(notes = "list of products", allowEmptyValue = false)
	private List<OrderItemTO> products;
	
	@ApiModelProperty(notes = "Total order amount", example = "120.39", allowEmptyValue = false)
	public BigDecimal getTotalOrderAmount() {
		BigDecimal sum = BigDecimal.ZERO;
		for (OrderItemTO item : products) {
			sum = sum.add(item.getPrice().multiply(new BigDecimal(item.getCount())));
		}
		return sum;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public LocalDateTime getCreateTimestamp() {
		return createTimestamp;
	}

	public void setCreateTimestamp(LocalDateTime createTimestamp) {
		this.createTimestamp = createTimestamp;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<OrderItemTO> getProducts() {
		return products;
	}

	public void setProducts(List<OrderItemTO> products) {
		this.products = products;
	}
	
	

}
