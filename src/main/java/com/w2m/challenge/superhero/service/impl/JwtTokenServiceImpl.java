package com.w2m.challenge.superhero.service.impl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

import com.w2m.challenge.superhero.service.TokenService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JwtTokenServiceImpl implements TokenService {
	private static final String USERNAME_CLAIM_KEY = "username";
	private static final String ROLE_LIST_CLAIM_KEY = "roles";
	
	private long tokenExpirationInSeconds;
	private String secret;
	
	public JwtTokenServiceImpl(long tokenExpirationInSeconds, String tokenSignatureSecret) {
		assert(tokenSignatureSecret != null);
		this.tokenExpirationInSeconds = tokenExpirationInSeconds;
		this.secret = tokenSignatureSecret;
	}

	public String generateTokenFor(String username, String commaSeparatedRoles) {
		Map<String, String> customClaims = Map.of(
				USERNAME_CLAIM_KEY, username,
				ROLE_LIST_CLAIM_KEY, commaSeparatedRoles);
		var claims = buildClaims(customClaims);
		return buildJwtWithClaims(claims);
	}
	
	private Claims buildClaims(final Map<String, String> customClaims) {
		var now = LocalDateTime.now();
		var claims = Jwts
				.claims()
				.setIssuedAt(fromLocalDateTime(now))
				.setExpiration(fromLocalDateTime(now.plusSeconds(tokenExpirationInSeconds)));
		claims.putAll(customClaims);
		return claims;
	}
	
	private String buildJwtWithClaims(Claims claims) {
		return Jwts
				.builder()
				.setHeaderParam("typ", "JWT")
				.setClaims(claims)
				.signWith(SignatureAlgorithm.HS256, getBase64Secret())
				.compact();
	}
	
	private byte[] getBase64Secret() {
		return Base64.getEncoder().encode(secret.getBytes());
	}
	
	// Maybe the following method is not a good fit for this class, neither is it worth to create a whole
	// new class or interface just to move this only method in a small project like this one.
	private Date fromLocalDateTime(LocalDateTime time) {
		return Date.from(time.atZone(ZoneId.systemDefault()).toInstant());
	}

}
