package com.example.DuregshContactProject.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.DuregshContactProject.entity.User;

public interface UserDao  extends JpaRepository<User, Integer>{

	
	
	//create for pass username change to email
	@Query("select u from User u where u.name=:name")
	public User getUserByUserName(@Param("name")String name);
	
	@Query("select u from User u where u.email=:email")
	public User getUserByUserEmail(@Param("email")String email);
	
	
}
