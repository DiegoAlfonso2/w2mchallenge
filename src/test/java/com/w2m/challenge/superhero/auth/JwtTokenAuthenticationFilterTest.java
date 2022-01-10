package com.w2m.challenge.superhero.auth;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.w2m.challenge.superhero.test.helpers.AuthTokenTestHelper;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
public class JwtTokenAuthenticationFilterTest implements AuthTokenTestHelper {

	@Mock
	private HttpServletRequest mockRequest;
	
	@Mock
	private RequestMatcher mockRequestMatcher;
	
	@Mock
	private Authentication mockAuthentication;
	
	@Captor
	private ArgumentCaptor<Authentication> authCaptor;

	@Mock
	private AuthenticationManager mockAuthManager;
	
	private JwtTokenAuthenticationFilter filterUnderTest;
	
	@BeforeEach
	public void setUp() {
		filterUnderTest = new JwtTokenAuthenticationFilter(mockRequestMatcher, mockAuthManager);
	}
	
	@Test
	public void shouldExtractTokenFromHeaderAndAskManagerToPerformAuthentication() throws AuthenticationException, IOException, ServletException {
		when(mockRequest.getHeader("Authorization")).thenReturn("Bearer " + TEST_VALID_TOKEN);
		when(mockAuthManager.authenticate(any(Authentication.class))).thenReturn(mockAuthentication);
		var auth = filterUnderTest.attemptAuthentication(mockRequest, null);
		verify(mockAuthManager).authenticate(authCaptor.capture());
		assertTrue(auth == mockAuthentication);
		assertInstanceOf(UsernamePasswordAuthenticationToken.class, authCaptor.getValue());
		UsernamePasswordAuthenticationToken capturedAuth = (UsernamePasswordAuthenticationToken) authCaptor.getValue();
		assertEquals(TEST_VALID_TOKEN, capturedAuth.getPrincipal());
		assertEquals(TEST_VALID_TOKEN, capturedAuth.getCredentials());
	}
	
}
