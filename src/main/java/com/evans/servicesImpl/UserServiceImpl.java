package com.evans.servicesImpl;

import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.evans.entity.User;
import com.evans.entity.VerificationToken;
import com.evans.exceptions.UserAlreadyExistsException;
import com.evans.registration.RegistrationRequest;
import com.evans.repository.IUserRepository;
import com.evans.repository.IVerificationTokenRepository;
import com.evans.services.IUserService;

@Service
public class UserServiceImpl implements IUserService {

	@Autowired
	private IUserRepository iUserRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private IVerificationTokenRepository iVerificationTokenRepository;

	@Override
	public List<User> getUSers() {
		return iUserRepository.findAll();
	}

	@Override
	public User registerUser(RegistrationRequest request) {
		Optional<User> user = this.findByEmail(request.email());
		if (user.isPresent()) {
			throw new UserAlreadyExistsException("User with email " + request.email() + " already exists");
		}
		var newUser = new User();
		newUser.setFirstname(request.firstname());
		newUser.setLastname(request.lastname());
		newUser.setEmail(request.email());
		newUser.setPassword(passwordEncoder.encode(request.password()));
		newUser.setRole(request.role());
		return iUserRepository.save(newUser);
	}

	@Override
	public Optional<User> findByEmail(String email) {
		return iUserRepository.findByEmail(email);
	}

	@Override
	public void saveUserVerificationToken(User user, String token) {
		var verificationToken = new VerificationToken(token, user);
		iVerificationTokenRepository.save(verificationToken);
	}

	@Override
	public String validateToken(String theToken) {
		VerificationToken token = iVerificationTokenRepository.findByToken(theToken);
		if (token == null) {
			return "Invalid verification token";
		}
		User user = token.getUser();
		Calendar calendar = Calendar.getInstance();
		if ((token.getExpirationTime().getTime() - calendar.getTime().getTime()) < 0) {
			iVerificationTokenRepository.delete(token);
			return "Token already expired";
		}
		user.setEnabled(true);
		iUserRepository.save(user);

		return "valid";
	}

}
