package com.bookshop.to;

import java.util.List;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


@ApiModel(description = "Information of a order")
public class OrderPutTO {
	
	@Email
	@ApiModelProperty(notes = "Buyer’s e-mail", example = "stefan@irle.com", allowEmptyValue = false)
	private String email; 

	@NotNull
	@Size(min = 1)
	@ApiModelProperty(notes = "list of products", allowEmptyValue = false)
	private List<OrderItemPutTO> products;
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<OrderItemPutTO> getProducts() {
		return products;
	}

	public void setProducts(List<OrderItemPutTO> products) {
		this.products = products;
	}
	
	

}
