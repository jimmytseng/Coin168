package com.vjtech.coin168.service.impl;

import java.io.File;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.vjtech.coin168.exception.InfoException;

@Service
public class MailService {

	@Autowired
	private JavaMailSender mailSender;

	public void sendSimpleMessage(String to, String subject, String text) {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom("ad@coin168.cc");
		message.setTo(to);
		message.setSubject(subject);
		message.setText(text);
		mailSender.send(message);
	}

	public void sendMessageWithAttachment(String to, String subject, String text, String pathToAttachment,
			String attachName) {
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper;
		try {
			helper = new MimeMessageHelper(message, true);
			helper.setFrom("ad@coin168.cc");
			helper.setTo(to);
			helper.setSubject(subject);
			helper.setText(text);
			FileSystemResource file = new FileSystemResource(new File(pathToAttachment));
			helper.addAttachment("attachName", file);
		} catch (MessagingException e) {
			throw new InfoException("發送郵件錯誤");
		}
		mailSender.send(message);
	}

	@Async
	public void sendWithHtml(String to, String subject, String htmlMsg) {
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		try {
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
			helper.setFrom("ad@coin168.cc");
//			mimeMessage.setContent(htmlMsg, "text/html"); /** Use this or below line **/
			helper.setText(htmlMsg, true); // Use this or above line.
			helper.setTo(to);
			helper.setSubject(subject);
		} catch (MessagingException e) {
			throw new InfoException("發送郵件錯誤");
		}
		mailSender.send(mimeMessage);
	}

}
