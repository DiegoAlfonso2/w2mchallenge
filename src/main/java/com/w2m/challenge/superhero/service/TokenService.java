package com.w2m.challenge.superhero.service;

import java.util.Map;

public interface TokenService {

	String generateTokenFor(String username, String commaSeparatedRoles);
	Map<String, Object> getTokenClaims(String token);
}
