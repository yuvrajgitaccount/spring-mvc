package com.example.DuregshContactProject.service;

import org.springframework.stereotype.Service;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;



@Service
public class EmailService {

	
	public boolean sendEmail(String subject,String message,String to)
	{
		//rest of code
		boolean flag=false;
	
		
		String from="jubrajmohanty@gmail.com";
		
		//declare varibale
			String host="smtp.gmail.com";
			
			//declear Properties
			
			Properties properties=System.getProperties();
			System.out.println("PROPERTIES"+properties);
			
			
			//setting imp[orant inforantion to properties object
			
				
			//host
			
			properties.put("mail.smtp.host", host);
			properties.put("mail.smtp.port","465");
			
			properties.put("mail.smtp.ssl.enable","true");
			properties.put("mail.smtp.auth","true");
			
			
			
			
			//Step 1 to get the session object
		Session session=Session .getInstance(properties, new Authenticator() {

			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				// TODO Auto-generated method stub
				return new PasswordAuthentication("jubrajmohanty@gmail.com","MOMYMOMY");
			}
			
			
			
		});
		
		session.setDebug(true);
			//compose the message[text,multimidea]
		
		MimeMessage m=new MimeMessage(session);
		
		//from email
		try {
			m.setFrom(from);
			
			//adding recipent to message
			m.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
			
			//Adding subject 2 messiage
			m.setSubject(subject);
			
			//addding text to message
			
			 // Send the actual HTML message, as big as you like
			   m.setContent(message,"text/html");
			
			
			//send
			
			//step 3:send the message using Transport class
			Transport.send(m);
			
			System.out.println("Sent success------------------");
			flag=true;
			
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			return flag;
			
		}
}
