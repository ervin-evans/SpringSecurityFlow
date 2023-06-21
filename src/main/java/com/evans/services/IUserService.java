package com.evans.services;

import java.util.List;
import java.util.Optional;

import com.evans.entity.User;
import com.evans.registration.RegistrationRequest;

public interface IUserService {
	public List<User> getUSers();
	public User registerUser(RegistrationRequest request);
	public Optional<User> findByEmail(String email);

}
