package com.w2m.challenge.superhero.service;

import com.w2m.challenge.superhero.model.auth.User;

public interface UserAuthService {

	String authenticate(String username, String password);
	User getUserDetailsFromToken(String token);
}
