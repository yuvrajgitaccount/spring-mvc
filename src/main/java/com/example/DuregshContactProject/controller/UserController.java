package com.example.DuregshContactProject.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.example.DuregshContactProject.dao.ContactDao;
import com.example.DuregshContactProject.dao.MyhOrdeRepo;
import com.example.DuregshContactProject.dao.UserDao;
import com.example.DuregshContactProject.entity.Contact;
import com.example.DuregshContactProject.entity.MyOrder;
import com.example.DuregshContactProject.entity.User;
import com.example.DuregshContactProject.message.Message;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

@Controller
@RequestMapping("/user")
public class UserController {

	
	@Autowired
	private UserDao dao;
	
	@Autowired
	private ContactDao contact;
	//it is using for user dashbord

	@Autowired
	private MyhOrdeRepo order;
	//common data for every method
	@ModelAttribute
	public void addCommondata(Model model,Principal principal,@ModelAttribute("user")User user)
	{
		
		System.out.println(user);
		String usernameString=	principal.getName();
		
		System.out.println("UserName"+usernameString);
		
	User user1=dao.getUserByUserName(usernameString);
	System.out.println("User"+user1);
	model.addAttribute("user",user1);	
	}
	
	//it is using for user dashbord
	@RequestMapping("/index")
	public String dashbord(Model model,Principal principal)
	
	{
		model.addAttribute("title","UserDashBord");
		
		return"/normal/user_dashboard";
	}

	
	//add contact form 
	@GetMapping(value = "/add-contact")
	public String openContactForm(Model model)
	{
		model.addAttribute("title","Add Contact");
		
		model.addAttribute("contact",new Contact());
		
		return"/normal/add_contact_form";
	}
	
	
	@PostMapping("/process-contact")
	public String AddContact(@ModelAttribute Contact contact,@RequestParam("profileImage")MultipartFile multipartFile,Principal principal,HttpSession session)
	{
		try {
			
	
		//step 2 get the name
		
	String username =principal.getName();
		 
	User user=dao.getUserByUserName(username);
	
	//step 3 for set in contat
	contact.setUser(user);
	
	
	//chacking file is prent or not
	
	if (multipartFile.isEmpty()) {
		
		
		System.out.println("file is empty");
		contact.setImage("contact.jpg");
	}
	
	else {
		
		//chaeck that image using original image
		contact.setImage(multipartFile.getOriginalFilename());
		
		
		//store in to file location
		File file=new ClassPathResource("static/img").getFile();
		
		
		//
		Path path=Paths.get(file.getAbsolutePath()+File.separator+multipartFile.getOriginalFilename());
		
		//copy the file
		Files.copy(multipartFile.getInputStream(), path,StandardCopyOption.REPLACE_EXISTING);
		System.out.println("image is uploaded");
	}
	
	
	
	//step 4 get the contact form user
	
	user.getContacts().add(contact);
	
	
	//step 5 to save the user data
	
	this.dao.save(user);
		
		//step 1 for checking
		System.out.println("Added data"+contact);
		
		
		
		
		  session.setAttribute("message", new Message("Susssfully added success","alert-success"));
		  
		}
		catch (Exception e) {
			// TODO: handle exception
			
			e.printStackTrace();
			System.out.println("erroe"+e.getMessage());
		
			  session.setAttribute("message", new Message("wrong contact","alert-danger"));
			  
		}
		
		return"/normal/add_contact_form";
		
	}
	
	//show Contact
	@GetMapping("/show-contact/{page}")

	public String showContact(@PathVariable("page")Integer page,Model m,Principal principal)
	{
		m.addAttribute("title", "showContact");
		
		
		String userName=principal.getName();
		User user =dao.getUserByUserName(userName);
		
		//page and size pf page
		//pageable
	Pageable pageable=PageRequest.of(page, 3);
		  Page<Contact>  contacts= this.contact.findContactByUser(user.getId(),pageable);
		  
		  m.addAttribute("contacts", contacts);
		  m.addAttribute("currentPage", page);
		  m.addAttribute("totalPages", contacts.getTotalPages());
		  
		return "/normal/show_contact_form";
		
	}
		
	
	
	
	//showing paticular contact deatails


	@RequestMapping("/{cId}/contact")
	public String showContactDetail(@PathVariable("cId")Integer cId,Model model,Principal principal)
	{
		
		System.out.println("cid"+cId);
		
		Optional<Contact>optnial=this.contact.findById(cId);
		
		    Contact contact= optnial.get();
		    
		    
		  String nameString=  principal.getName();
		  
	User  user	=  dao.getUserByUserName(nameString);

	if(user.getId()==contact.getUser().getId())
		    
	{
		    model.addAttribute("contact", contact);
	}
		
		return "/normal/contact_detail";
	}
		
	
	
	
	@GetMapping("/delete/{cid}")
	public String deleteContact(@PathVariable("cid")Integer cId,Model model,HttpSession session)
	{
	Optional<Contact>optinal= this.contact.findById(cId);

	  Contact contact=  optinal.get();
	  
	  
	  System.out.println("Contact"+contact.getcId());
	  
	  //handling the bug
	  
	  //it is using for unlink with before delete the contact bacase we use casecase.all
	  contact.setUser(null);
	 
	  this.contact.delete(contact);
	  
	  System.out.println("deleteed");
	  session.setAttribute("message", new Message("Susssfully deleted","alert-success"));
	  
	  
	return "redirect:/user/show-contact/0";

	}
	
	
	
	//open update form handler
	@PostMapping("/update-contact/{cid}")
	public String UpdataForm(@PathVariable("cid")Integer cid,Model model)
	{
		Contact contact=this.contact.findById(cid).get();
		model.addAttribute("contact",contact);
		return "/normal/update_form";
	}
	
	
	
	

	//open update contact handler
	@RequestMapping(value = "/process-update",method = RequestMethod.POST)
	public String UpdateHandler(@ModelAttribute Contact contact,@RequestParam("profileImage")MultipartFile file,Model model,HttpSession session,Principal principal)
	{
		try {
			//old contact deatils
			Contact olContact=this.contact.findById(contact.getcId()).get();
			
			
			//image upload
			if(!file.isEmpty())
			{
				//file work
				//rewrite
				
				//delete photo
				 File deletefile=	new ClassPathResource("static/img").getFile();
				 File file1=new File(deletefile,olContact.getImage());
				 file1.delete();
				
				//update photo
			  	//giving the path where the image is set
			    File file2=	new ClassPathResource("static/img").getFile();
			    
			    //giving the read file path
			    Path path=Paths.get(file2.getAbsolutePath()+File.separator+file.getOriginalFilename());
			    
			    //copy the path
			    	Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
			    	System.out.println("image is uploaded");
			    	
			    	contact.setImage(file.getOriginalFilename());
				
			}
			
			else {
				contact.setImage(olContact.getImage());
			}
			User user=this.dao.getUserByUserName(principal.getName());
			contact.setUser(user);
			this.contact.save(contact);
			session.setAttribute("message", new Message("Your contact is updated","success"));
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		
		
		System.out.println("the Contact Name"+contact.getName());
		System.out.println("the id is Contact is "+contact.getcId());
		
		return "redirect:/user/"+contact.getcId()+"/contact";
		
	}
	
	//create profile

	@GetMapping("/profile")
	public String ProfileSave(Model model)
	{
		model.addAttribute("/title", "Profile picture");
		return "/normal/profile_form";
	}
	

	
	@GetMapping("/setting")
	public String openSettings(Model model)
{		
		
		
		model.addAttribute("/title", "setting page");
		
		return"/normal/change_password";
	}
	
	
	//craeteing order for payment
	
	@PostMapping("/create_order")
	@ResponseBody
	public String createOrder(@RequestBody Map<String,Object>data,Principal principal) throws RazorpayException
	{
	int amt=Integer.parseInt(data.get("amount").toString());
		
		
	
	RazorpayClient client=new RazorpayClient("rama","");
	
	JSONObject ob=new JSONObject();
	
	ob.put("amount", amt*100);
	ob.put("currency","INR");
	ob.put("recepipt","txn_235425");
	
	//createing order
	
	Order order=client.Orders.create(ob);
	
	System.out.println(order);
	
	
	
	//save the order in database
	
	MyOrder myorder=new MyOrder();
	
	myorder.setAmount(order.get("amount")+"");
	myorder.setMyOrderId(order.get("id"));
	myorder.setStatus("created");
	myorder.setPaymentId(null);
	
	
	myorder.setUser(this.dao.getUserByUserName(principal.getName()));
	
	myorder.setReceipt(order.get("recepit"));
	
	this.order.save(myorder);
	
	//if you want then save this to your data
	
		System.out.println(data);
		return order.toString();
		
	}
	
	@PostMapping("/update_order")
	public ResponseEntity<?>updateOrder(@RequestBody Map<String ,Object>data)
	{
		
		
	MyOrder myorder=	this.order.findByOrderId(data.get("order_id").toString());
	
	
	myorder.setPaymentId(data.get("payment_id").toString());
	myorder.setStatus(data.get("status").toString());
	
	this.order.save(myorder);
		System.out.println("data");
		
		
		
		return ResponseEntity.ok("updated");
		
	}
	
	
}
