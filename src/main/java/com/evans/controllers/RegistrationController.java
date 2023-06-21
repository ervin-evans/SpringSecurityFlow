package com.evans.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.evans.entity.User;
import com.evans.registration.RegistrationRequest;
import com.evans.services.IUserService;

@RestController
@RequestMapping("/register")
public class RegistrationController {

	@Autowired
	private IUserService iUserService;

	@PostMapping
	public String registerUser(RegistrationRequest request) {
		User user = iUserService.registerUser(request);
		// public registration event

		return "Success!, Please check your email to complete your registration";
	}
}
