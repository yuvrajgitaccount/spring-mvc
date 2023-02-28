package com.example.DuregshContactProject.controller;

import java.util.Random;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.DuregshContactProject.dao.UserDao;
import com.example.DuregshContactProject.entity.User;
import com.example.DuregshContactProject.message.Message;
import com.example.DuregshContactProject.service.EmailService;

import javassist.expr.NewArray;

@Controller
public class ForgotController {
	
	
	@Autowired
	private UserDao dao;
	@Autowired
	private EmailService service;
	
	@Autowired
	private BCryptPasswordEncoder encode;
	
	@RequestMapping("/forget")
	public String forgetPassword()
	{
		return"forget_page";
	}


	
	@PostMapping("/sendotp")
	public String sendotppage(@RequestParam("email")String email,HttpSession session)
	
	{
		System.out.println("email"+email);
		
		
		Random random=new Random(1000);
	int otp=random.nextInt(9999999);
	System.out.println("otp"+otp);
	
	
	
	
	//write code for send otp to email
	String subject="OTP FROM SCM";
	String message=""
			+"<div style='border:1px solid #e2e2e2;padding:20px'>"
			+"<h1>"
			+"OTP is"
			+"<b>"+otp
			+"</n>"
			+"</h1>"
			+"</div>";
	String to=email;
	
	
	boolean flag=this.service.sendEmail(subject, message, to);
	
	
	if (flag) {
		session.setAttribute("myotp",otp);
		session.setAttribute("email", email);
		
		return"otp_page";
		
	}
		
	
	//error message
	session.setAttribute("message",new Message("please enter your coorent email id","alert-danger"));
	
	
		return"forget_page";
	}

	
	
	//verify otp
	@PostMapping("/verify-otp")
	public String verifyOtp(@RequestParam("otp")int otp,HttpSession session)
	{
		
		
		int myOtpInteger = (int)session.getAttribute("myotp");
		String email=(String)session.getAttribute("email");
		System.out.println("email"+email);
		
		if (myOtpInteger==otp) {
			
		User user=	dao.getUserByUserEmail(email);
			
			if (user==null) {
				
				
				
				
				session.setAttribute("message",new Message("User doesnot exsit","alert-danger"));
				
				
				return"forget_page";
				//error message
			}
			
			else {
				
			}
			
			
			
			
			return"password_change_form";
			
		}
		else {
			
			//error message
			session.setAttribute("message",new Message("please enter your valid otp","alert-danger"));
			
			return "otp_page";
		}
		
		
	}
	
	//change password and save
	@PostMapping("/change-password")
	public String changeandStringsavePassword(@RequestParam("newpassword")String newpassword,HttpSession session)
	{
		
		
		String email=(String)session.getAttribute("email");
		
		
		User user=dao.getUserByUserEmail(email);
		System.out.println("user gmnail"+user);
		
		user.setPassword(this.encode.encode(newpassword));
		
		User user2=dao.save(user);
		System.out.println("user"+user2);
		return"redirect:/signin?change=password changed successfully....";
		
		
		
	}
	
	
	
	
	
	
}
