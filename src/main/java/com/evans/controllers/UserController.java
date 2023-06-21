package com.evans.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.evans.entity.User;
import com.evans.services.IUserService;

@RestController
@RequestMapping("/users")
public class UserController {
	
	@Autowired
	private IUserService iUserService;
	
	@GetMapping
	public List<User> getUsers(){
		return iUserService.getUSers();
	}

}
