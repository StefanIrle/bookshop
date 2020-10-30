package com.bookshop.entity;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.GenericGenerator;

/**
 * Order Entity
 *
 */
@Entity
@Table(name = "BOOKORDER")
public class Order {
	
	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(name = "ID", updatable = false, nullable = false,unique = true)
	private String id;
	
	@NotBlank
	@Email
	@Column(name = "EMAIL", nullable = false, length = 200) 
	private String email;
	
	@NotNull
	@Column(name = "CREATE_TS", nullable = false) 
	private LocalDateTime createTimestamp;
	
	@NotNull
	@Size(min=1)
	@OneToMany(mappedBy="order", cascade=CascadeType.ALL)
	private List<OrderItem> items;
	
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

	public List<OrderItem> getItems() {
		return items;
	}

	public void setItems(List<OrderItem> items) {
		this.items = items;
	}

	
	
}
