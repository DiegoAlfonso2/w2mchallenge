package com.w2m.challenge.superhero.auth;

import java.util.Optional;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

import com.w2m.challenge.superhero.service.UserAuthService;

public class JwtTokenAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {
	
	private UserAuthService userAuthService;

	public JwtTokenAuthenticationProvider(UserAuthService userAuthService) {
		this.userAuthService = userAuthService;
	}

	@Override
	protected void additionalAuthenticationChecks(UserDetails userDetails,
			UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
		// Nothing to do here
	}

	@Override
	protected UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication)
			throws AuthenticationException {
		var token = Optional
				.ofNullable(authentication.getCredentials())
				.map(String::valueOf)
				.orElseThrow(() -> new BadCredentialsException("Empty credentials at Authentication Provider"));
		return userAuthService.getUserDetailsFromToken(token);
	}

}
