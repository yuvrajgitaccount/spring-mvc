package com.example.DuregshContactProject.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.example.DuregshContactProject.dao.ContactDao;
import com.example.DuregshContactProject.dao.UserDao;
import com.example.DuregshContactProject.entity.Contact;
import com.example.DuregshContactProject.entity.User;
import com.example.DuregshContactProject.message.Message;
import com.sun.xml.bind.v2.model.core.ErrorHandler;

@RestController
public class SearchController implements ErrorController {

	
	
	private final static String PATH="/error";
	@Autowired
	private UserDao dao;
	
	@Autowired
	private ContactDao contact;
	
	
//USING JSONIGNORE IN USER USER IN CONTACT ENTITY
	@GetMapping("/search/{query}")
	
	public ResponseEntity<?>Search(@PathVariable("query")String query,Principal principal)
	{
		
		System.out.println(query);
		
		
		User user= this.dao.getUserByUserName(principal.getName());
		
	List<Contact>contacts= this.contact.findByNameContainingAndUser(query, user);
		
		
		
		return ResponseEntity.ok(contacts);
		
	}

	
	@GetMapping(value = PATH)
	public String Message()
	{
		return "not resource found";
	}


	@Override
	public String getErrorPath() {
		// TODO Auto-generated method stub
		return PATH;
	}

}
