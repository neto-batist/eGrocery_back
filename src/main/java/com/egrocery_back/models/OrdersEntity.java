package com.egrocery_back.models;

import java.sql.Timestamp;
import java.util.Collection;
import java.util.Objects;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "orders", schema = "egrocery", catalog = "")
@AllArgsConstructor
@NoArgsConstructor
public class OrdersEntity {
	private Integer id;
	private Double totalPrice;
	private Timestamp createdAt;
	private Collection<OrderItemsEntity> orderItems;
	private Collection<OrderStatusEntity> orderStatuses;
	private UsersEntity usersByUserId;

	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	@Column(name = "id", nullable = false)
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Basic
	@Column(name = "total_price", nullable = false, precision = 2)
	public Double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(Double totalPrice) {
		this.totalPrice = totalPrice;
	}

	@Basic
	@Column(name = "created_at", nullable = true)
	public Timestamp getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		OrdersEntity that = (OrdersEntity) o;
		return Objects.equals(id, that.id) && Objects.equals(totalPrice, that.totalPrice)
				&& Objects.equals(createdAt, that.createdAt);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, usersByUserId, totalPrice, orderStatuses, createdAt);
	}

	@OneToMany(mappedBy = "ordersByOrderId")
	public Collection<OrderItemsEntity> getOrderItemsById() {
		return orderItems;
	}

	public void setOrderItemsById(Collection<OrderItemsEntity> orderItemsById) {
		this.orderItems = orderItemsById;
	}

	@OneToMany(mappedBy = "ordersByOrderId")
	public Collection<OrderStatusEntity> getOrderStatuses() {
		return orderStatuses;
	}

	public void setOrderStatuses(Collection<OrderStatusEntity> orderStatusesById) {
		this.orderStatuses = orderStatusesById;
	}

	@ManyToOne
	@JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
	public UsersEntity getUsersByUserId() {
		return usersByUserId;
	}

	public void setUsersByUserId(UsersEntity usersByUserId) {
		this.usersByUserId = usersByUserId;
	}
}