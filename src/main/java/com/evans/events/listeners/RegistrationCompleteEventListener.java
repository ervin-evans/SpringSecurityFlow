package com.evans.events.listeners;

import java.io.UnsupportedEncodingException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.evans.entity.User;
import com.evans.events.RegistrationCompleteEvent;
import com.evans.services.IUserService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent> {

	@Autowired
	private IUserService iUserService;

	@Autowired
	private JavaMailSender javaMailSender;

	private User theUser;

	@Override
	public void onApplicationEvent(RegistrationCompleteEvent event) {
		// 1.- Get the newly registered user
		theUser = event.getUser();
		// 2.- Create a verification token for the user
		String verificationToken = UUID.randomUUID().toString();
		// 3.- Save the verification token for the user
		iUserService.saveUserVerificationToken(theUser, verificationToken);
		// 4.- Build the verification url to be send to the user
		String url = event.getApplicationUrl() + "/register/verifyEmail?token=" + verificationToken;
		// 5.- Send the email
		try {
			sendVerificationEmail(url);
		} catch (MessagingException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		log.info("Click the link to complete your registration: {}", url);

	}

	public void sendVerificationEmail(String url) throws MessagingException, UnsupportedEncodingException {
		String subject = "Email verification";
		String senderName = "User Registration Portal Service";
		String emailContent = "<p>Hi, " + theUser.getFirstname() + "</p>"
				+ "<p>Thank you for registerin  with us. Please follow the link below to complete your registration</p>"
				+ "<a href=\"" + url + "\">Verify your email to active your account</a>"
				+ "<p>Thank you <br> User Registration Portal Service</p>";

		MimeMessage message = javaMailSender.createMimeMessage();
		var messageHelper = new MimeMessageHelper(message);
		messageHelper.setFrom("ervintdeveloper@gmail.com", senderName);
		messageHelper.setTo(theUser.getEmail());
		messageHelper.setSubject(subject);
		messageHelper.setText(emailContent, true);
		javaMailSender.send(message);
	}

}
