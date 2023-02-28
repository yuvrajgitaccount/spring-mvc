package com.example.DuregshContactProject.controller;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.DuregshContactProject.dao.UserDao;
import com.example.DuregshContactProject.entity.Contact;
import com.example.DuregshContactProject.entity.User;
import com.example.DuregshContactProject.message.Message;



@Controller
public class HomeController {
	
	
	@Autowired
	private UserDao dao;
	
	
	@Autowired
	private BCryptPasswordEncoder encoder;
	
	
	@RequestMapping(value ="/home",method = RequestMethod.GET)
	public String home(Model model)
	{
		
		model.addAttribute("title", " home page");
		return"home";
	}
	
	

	@RequestMapping(value ="/about",method = RequestMethod.GET)
	public String about(Model model)
	{
		
		model.addAttribute("title", " about page");
		return"about";
	}
	
	
	
	//sign up page
	@RequestMapping(value = "/signup",method = RequestMethod.GET)
	public String signUpPage(Model model)
	{
		model.addAttribute("title","signup page");
		
		model.addAttribute("user",new User());
		
		return"signup";
	}
	
	
	
	
	
	
	@RequestMapping(value="/do_register",method = RequestMethod.POST)
	public String registerPage(@Valid @ModelAttribute("user")User user,BindingResult result,@RequestParam(value = "agreement",defaultValue = "false")boolean agreement,Model model,HttpSession session )
	
	{
		
		
		try {
			
			//not stisfied conditions
			
			if (!agreement) {
				
				
			System.out.println("YOU HAVE NOT AGREED THE TERM ON CONDITIOPN ");	
			
			throw new Exception("YOU HAVE NOT AGREED THE TERM ON CONDITIOPN");
				
			}
			
			
	if (result.hasErrors()) {
				
		System.out.println("Eror" +result.toString());
		
		model.addAttribute("user", user);
		return "signup";
				
			}

			//it is stisfacied the condition
			
			user.setRole("ROLE_USER");
			user.setEnabled(true);
			user.setImageurl("contact.jpg");
			
			user.setPassword(encoder.encode(user.getPassword()));
			
			//print the user
			System.out.println("user"+user);
			
			//print agremment
			System.out.println("Agreement"+agreement);
			
			//save the data in to dao class
			
		User responseUser=	dao.save(user);
		
		//store the dao object
//		model.addAttribute("user", responseUser);
		
		//new blank user
		model.addAttribute("user",new  User());
		session.setAttribute("message", new Message("Susssfully registerd","alert-success"));
			return "signup";	
		} 

		//if exception accoure the 
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			
			//old user if it is fail
			model.addAttribute("user", user);
			session.setAttribute("message", new Message("something went to wrong!! "+e.getMessage(),"alert-danger"));
		}
		return "signup";
		
	}
	
	
	
	@GetMapping("/signin")
	public String customLogin(Model model)
	{
		return"login";
	}
	
	
	
	

	
//	@GetMapping("/test")
//	
//	@ResponseBody
//	public String home()
//	{
//		
//		User user=new User();
//		user.setName("yuvraj");
//		user.setEmail("jubrajMohanty");
//		
//		Contact contact=new Contact();
//		user.getContacts().add(contact);
//		
//		dao.save(user);
//		return"successfully registerd";
//		
//		
//		
//	}
//	

}
