package com.example.DuregshContactProject.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.DuregshContactProject.entity.Contact;
import com.example.DuregshContactProject.entity.User;

public interface ContactDao extends JpaRepository<Contact, Integer>{
	
	//using for pagintion
		@Query("from Contact as c where c.user.id =:userId")
		public Page<Contact>findContactByUser(@Param("userId")int userId,Pageable pageable);
		
		public List<Contact> findByNameContainingAndUser(String keyword,User user);

}
