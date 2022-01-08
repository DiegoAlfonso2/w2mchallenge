package com.w2m.challenge.superhero.service.impl;

import org.apache.commons.codec.digest.DigestUtils;

import com.w2m.challenge.superhero.repository.UserRepository;
import com.w2m.challenge.superhero.service.TokenService;
import com.w2m.challenge.superhero.service.UserAuthService;

public class UserAuthServiceImpl implements UserAuthService {

	private UserRepository userRepository;
	private TokenService tokenService;
	
	public UserAuthServiceImpl(UserRepository userRepository, TokenService tokenService) {
		this.userRepository = userRepository;
		this.tokenService = tokenService;
	}

	public String authenticate(String username, String password) {
		var hashedPassword = DigestUtils.sha256Hex(username + password);
		// TODO don't forget to add negative test cases that will force to refactor the following line
		var user = userRepository.findByUsernameAndHashedPassword(username, hashedPassword).get();
		return tokenService.generateTokenFor(user.getUsername(), user.getRoles());
	}

}
