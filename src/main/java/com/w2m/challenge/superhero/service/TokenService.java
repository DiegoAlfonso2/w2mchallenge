package com.w2m.challenge.superhero.service;

import java.util.Map;

public interface TokenService {
	String USERNAME_CLAIM_KEY = "username";
	String ROLE_LIST_CLAIM_KEY = "roles";

	String generateTokenFor(String username, String commaSeparatedRoles);
	Map<String, Object> getTokenClaims(String token);
}
