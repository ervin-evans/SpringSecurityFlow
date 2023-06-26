package com.evans.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.evans.repository.IUserRepository;

@Service
public class UserDetailService implements UserDetailsService {

	@Autowired
	private IUserRepository iUserRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		return iUserRepository.findByEmail(email).map(UserRegistrationDetails::new)
				.orElseThrow(() -> new UsernameNotFoundException("User Not Found"));
	}

}
