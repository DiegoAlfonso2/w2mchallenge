package com.w2m.challenge.superhero.service.impl;

import org.apache.commons.codec.digest.DigestUtils;

import com.w2m.challenge.superhero.repository.UserRepository;
import com.w2m.challenge.superhero.service.UserAuthService;

public class UserAuthServiceImpl implements UserAuthService {

	private UserRepository userRepository;
	
	public UserAuthServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public String authenticate(String username, String password) {
		var hashedPassword = DigestUtils.sha256Hex(username + password);
		var user = userRepository.findByUsernameAndHashedPassword(username, hashedPassword).get();
		return "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";
	}

}
