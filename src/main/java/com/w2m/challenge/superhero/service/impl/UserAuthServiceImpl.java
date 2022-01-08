package com.w2m.challenge.superhero.service.impl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;

import com.w2m.challenge.superhero.repository.UserRepository;
import com.w2m.challenge.superhero.service.UserAuthService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class UserAuthServiceImpl implements UserAuthService {

	private UserRepository userRepository;
	
	public UserAuthServiceImpl(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public String authenticate(String username, String password) {
		var hashedPassword = DigestUtils.sha256Hex(username + password);
		var user = userRepository.findByUsernameAndHashedPassword(username, hashedPassword).get();
		var now = LocalDateTime.now();
		Map<String, String> customClaims = Map.of(
				"username", user.getUsername(),
				"roles", user.getRoles());
		Claims claims = Jwts
				.claims()
				.setIssuedAt(Date.from(now.atZone(ZoneId.systemDefault()).toInstant()))
				.setExpiration(Date.from(now.plusSeconds(60).atZone(ZoneId.systemDefault()).toInstant()));
		claims.putAll(customClaims);
		
		return Jwts
				.builder()
				.setHeaderParam("typ", "JWT")
				.setClaims(claims)
				.signWith(SignatureAlgorithm.HS256, Base64.getEncoder().encode("secret".getBytes()))
				.compact();
	}

}
