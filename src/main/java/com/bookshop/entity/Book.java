package com.bookshop.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;

/**
 * Book Entity
 *
 */
@Entity
@Table(name="BOOK")
public class Book {
	
	@Id
	@GeneratedValue(generator = "UUID")
	@GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(name = "d", updatable = false, nullable = false,unique = true)
	private String id;
	
	@NotBlank
	@Column(name = "NAME", nullable = false, length = 50) 
	private String name;
	
	@NotNull
	@Min(value = 0)
	@Column(name = "PRICE", nullable = false) 
	private BigDecimal price;
	
	@NotNull
	@Column(name = "CREATE_TS", nullable = false) 
	private LocalDateTime createTimestamp;
	
	@Column(name = "DELETE_TS", nullable = true) 
	private LocalDateTime deletionTimestamp;
	
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

	public LocalDateTime getDeletionTimestamp() {
		return deletionTimestamp;
	}

	public void setDeletionTimestamp(LocalDateTime deletionTimestamp) {
		this.deletionTimestamp = deletionTimestamp;
	}
	
	
}
