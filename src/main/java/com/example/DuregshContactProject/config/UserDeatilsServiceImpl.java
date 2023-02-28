package com.example.DuregshContactProject.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.example.DuregshContactProject.dao.UserDao;
import com.example.DuregshContactProject.entity.User;

public class UserDeatilsServiceImpl implements UserDetailsService {

	
	
	@Autowired
	private UserDao dao;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		
		
		User user=dao.getUserByUserName(username);
		
		if (user==null) {
			
			throw new UsernameNotFoundException("Could not be found the user");
		}
		
	CustomDetails customDetails=new CustomDetails(user);
		
		return customDetails;
	}

}
