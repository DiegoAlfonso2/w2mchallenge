package com.w2m.challenge.superhero.service.impl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import com.w2m.challenge.superhero.service.TokenService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;

@Service
public class JwtTokenServiceImpl implements TokenService {
		
	private long tokenExpirationInSeconds;
	private String secret;
	
	public JwtTokenServiceImpl(
			@Value("${jwt.expiration.seconds:60}") long tokenExpirationInSeconds, 
			@Value("${jwt.signing.secret:secret}") String tokenSignatureSecret) {
		assert(tokenSignatureSecret != null);
		this.tokenExpirationInSeconds = tokenExpirationInSeconds;
		this.secret = tokenSignatureSecret;
	}

	@Override
	public String generateTokenFor(String username, String commaSeparatedRoles) {
		Map<String, String> customClaims = Map.of(
				USERNAME_CLAIM_KEY, username,
				ROLE_LIST_CLAIM_KEY, commaSeparatedRoles);
		var claims = buildClaims(customClaims);
		return buildJwtWithClaims(claims);
	}
	
	// TODO test what happens if token body has no claims (as parseClaimsJws will throw an exception in this case)
	@Override
	public Map<String, Object> getTokenClaims(String token) {
		try {
			var claims = Jwts
					.parser()
					.setSigningKey(secret.getBytes())
					.parseClaimsJws(token)
					.getBody();
			return Map.copyOf(claims);
		} catch (ExpiredJwtException eje) {
			throw new BadCredentialsException("Token is expired", eje);
		} catch (SignatureException se) {
			throw new BadCredentialsException("Apocryphal token: invalid signature", se);
		} catch (Exception e) {
			throw new BadCredentialsException("Error parsing JWT", e);
		}
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
				.signWith(SignatureAlgorithm.HS256, secret.getBytes())
				.compact();
	}
	
	// Maybe the following method is not a good fit for this class, neither is it worth to create a whole
	// new class or interface just to move this only method in a small project like this one.
	private Date fromLocalDateTime(LocalDateTime time) {
		return Date.from(time.atZone(ZoneId.systemDefault()).toInstant());
	}

}
