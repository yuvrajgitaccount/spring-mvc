package com.example.DuregshContactProject.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.DuregshContactProject.entity.MyOrder;

public interface MyhOrdeRepo extends JpaRepository<MyOrder,Long> {
	
	
	public MyOrder findByOrderId(String orderId);

}
