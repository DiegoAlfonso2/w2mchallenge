package com.w2m.challenge.superhero.auth;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

public class JwtTokenAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

	private final static String AUTHORIZATION_HEADER = "Authorization";
	private final static String TOKEN_PREFIX = "Bearer ";
	
	protected JwtTokenAuthenticationFilter(RequestMatcher requiresAuthenticationRequestMatcher,
			AuthenticationManager authenticationManager) {
		super(requiresAuthenticationRequestMatcher, authenticationManager);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {
		var token = Optional
				.ofNullable(request.getHeader(AUTHORIZATION_HEADER))
				.filter(header -> header.startsWith(TOKEN_PREFIX))
				.map(p -> p.substring(TOKEN_PREFIX.length()))
				.orElseThrow(() -> new BadCredentialsException("Header 'Authorization: Bearer xxx.yyy.zzz' not found in request"));
		var auth = new UsernamePasswordAuthenticationToken(token, token);
		return getAuthenticationManager().authenticate(auth);
	}

}
