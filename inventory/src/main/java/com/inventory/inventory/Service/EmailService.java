package com.inventory.inventory.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
	
	@Autowired
    private JavaMailSender javaMailSender;
	    
    void sendEmail(String to, String title, String text) {

        SimpleMailMessage mail = new SimpleMailMessage();
        mail.setTo(to);       
        mail.setSubject(title);
        mail.setText(text);

        javaMailSender.send(mail);

    }

}
