package com.vjtech.coin168.configuration;

import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class MailConfiguration {

	
	@Bean
	public JavaMailSender getJavaMailSender() {
	    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
	    mailSender.setHost("smtpout.secureserver.net");
	    mailSender.setPort(80);
	    
	    mailSender.setUsername("ad@coin168.cc");
	    mailSender.setPassword("Cycu19880902");
	    
	    Properties props = mailSender.getJavaMailProperties();
	    props.put("mail.transport.protocol", "smtp");
	    props.put("mail.smtp.auth", "true");
	    props.put("mail.smtp.starttls.enable", "true");
	    props.put("mail.debug", "false");
	    props.put("mail.smtp.ssl.trust","*");
	    mailSender.setJavaMailProperties(props);
	    return mailSender;
	}
	
}
