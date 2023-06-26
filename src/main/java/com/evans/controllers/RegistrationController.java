package com.evans.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.evans.entity.User;
import com.evans.entity.VerificationToken;
import com.evans.events.RegistrationCompleteEvent;
import com.evans.registration.RegistrationRequest;
import com.evans.repository.IVerificationTokenRepository;
import com.evans.services.IUserService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/register")
public class RegistrationController {

	@Autowired
	private IUserService iUserService;

	@Autowired
	private ApplicationEventPublisher publisher;

	@Autowired
	private IVerificationTokenRepository iVerificationTokenRepository;

	@PostMapping
	public String registerUser(@RequestBody RegistrationRequest registrationRequest, final HttpServletRequest request) {
		System.out.println(registrationRequest);
		User user = iUserService.registerUser(registrationRequest);

		publisher.publishEvent(new RegistrationCompleteEvent(user, applicationUrl(request)));
		return "Success!, Please check your email to complete your registration";
	}

	@GetMapping("/verifyEmail")
	public String verifyEmail(@RequestParam("token") String token) {
		VerificationToken theToken = iVerificationTokenRepository.findByToken(token);
		if (theToken.getUser().isEnabled()) {
			return "This account has already been verified, please login";
		}
		String verificationResult = iUserService.validateToken(token);
		if (verificationResult.equalsIgnoreCase("valid")) {
			return "Email verified successfully. Now you can login";
		}
		return "Invalid verification token";

	}

	private String applicationUrl(HttpServletRequest request) {
		return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
	}

}
