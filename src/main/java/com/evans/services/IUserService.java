package com.evans.services;

import java.util.List;
import java.util.Optional;

import com.evans.entity.User;
import com.evans.entity.VerificationToken;
import com.evans.registration.RegistrationRequest;

public interface IUserService {
	public List<User> getUSers();

	public User registerUser(RegistrationRequest request);

	public void saveUserVerificationToken(User user, String token);

	public Optional<User> findByEmail(String email);

	public String validateToken(String theToken);

}
