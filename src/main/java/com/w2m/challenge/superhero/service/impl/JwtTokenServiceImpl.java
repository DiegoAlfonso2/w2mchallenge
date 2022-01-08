package com.w2m.challenge.superhero.service.impl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JwtTokenServiceImpl {

	public String generateTokenFor(String username, String commaSeparatedRoles) {
		var now = LocalDateTime.now();
		Map<String, String> customClaims = Map.of(
				"username", username,
				"roles", commaSeparatedRoles);
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
