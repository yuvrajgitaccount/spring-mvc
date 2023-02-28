package com.example.DuregshContactProject.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="order")
public class MyOrder {
	
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long myOrderId;
	
	private String orderId;
	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	private String amount;
	
	private String receipt;
	
	private String status;

	@ManyToOne
	@JoinColumn(name="user_id")
	private User user;
	
	
	private String paymentId;
	
	

	public String getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}

	public Long getMyOrderId() {
		return myOrderId;
	}

	public void setMyOrderId(Long myOrderId) {
		this.myOrderId = myOrderId;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getReceipt() {
		return receipt;
	}

	public void setReceipt(String receipt) {
		this.receipt = receipt;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public MyOrder(Long myOrderId, String amount, String receipt, String status, User user) {
		super();
		this.myOrderId = myOrderId;
		this.amount = amount;
		this.receipt = receipt;
		this.status = status;
		this.user = user;
	}

	public MyOrder() {
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public String toString() {
		return "MyOrder [myOrderId=" + myOrderId + ", orderId=" + orderId + ", amount=" + amount + ", receipt="
				+ receipt + ", status=" + status + ", user=" + user + ", paymentId=" + paymentId + "]";
	}

	

	
	
}
