package com.bookshop.to;

import java.math.BigDecimal;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Details of a book")
public class BookPutTO {
	
	@NotBlank
	@Length(max = 50)
	@ApiModelProperty(notes = "The name of the book", example = "Java", required = true)
	private String name;
	
	@NotNull
	@Min(value = 0)
	@ApiModelProperty(notes = "The price of the book", example = "42.00", required = true)
	private BigDecimal price;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	
}
