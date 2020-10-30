package com.bookshop.to;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


@ApiModel(description = "Details of a book")
public class BookTO {
	
	@NotBlank
	@ApiModelProperty(notes = "Unique ID", example = "712ccbab-0071-4700-9249-6cd4a709d3cb", allowEmptyValue = false)
	private String id;
	
	@NotBlank
	@Length(max = 50)
	@ApiModelProperty(notes = "The name of the book", example = "Java", allowEmptyValue = false)
	private String name;
	
	@NotNull
	@ApiModelProperty(notes = "The price of the book", example = "42.00",allowEmptyValue = false)
	private BigDecimal price;
	
	@ApiModelProperty(notes = "Timestamp of creation", allowEmptyValue = false)
	private LocalDateTime createTimestamp;
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

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

	public LocalDateTime getCreateTimestamp() {
		return createTimestamp;
	}

	public void setCreateTimestamp(LocalDateTime createTimestamp) {
		this.createTimestamp = createTimestamp;
	}

}
