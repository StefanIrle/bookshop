package com.bookshop.to;


import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


@ApiModel(description = "Item of a order")
public class OrderItemPutTO {
	

	@NotBlank
	@Length(max = 36)
	@ApiModelProperty(notes = "The id of the item. (Id of book)", example = "a9bd6f23-cab1-4a1f-b235-a75dadc241aa", allowEmptyValue = false)
	private String productId;


	@NotNull
	@ApiModelProperty(notes = "Count of items", example = "5",allowEmptyValue = false)
	private Integer count;


	public String getProductId() {
		return productId;
	}


	public void setProductId(String productId) {
		this.productId = productId;
	}


	public Integer getCount() {
		return count;
	}


	public void setCount(Integer count) {
		this.count = count;
	}

}
