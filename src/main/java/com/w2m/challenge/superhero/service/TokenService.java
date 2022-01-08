package com.w2m.challenge.superhero.service;

public interface TokenService {

	String generateTokenFor(String username, String commaSeparatedRoles);
}
