package com.w2m.challenge.superhero.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.w2m.challenge.superhero.model.auth.User;
import com.w2m.challenge.superhero.repository.UserRepository;
import com.w2m.challenge.superhero.service.TokenService;
import com.w2m.challenge.superhero.test.helpers.AuthTokenTestHelper;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
public class UserAuthServiceImplTest implements AuthTokenTestHelper {

	private UserAuthServiceImpl serviceUnderTest;
	
	@Mock
	UserRepository userRepository;
	
	@Mock
	TokenService tokenService;
	
	@BeforeEach
	public void setUp() {
		serviceUnderTest = new UserAuthServiceImpl(userRepository, tokenService);
	}

	@Test
	public void shouldDelegateOnUserRepositoryToGetUserDetailsForTokenGeneration() throws JsonMappingException, JsonProcessingException, ParseException {
		var user = new User(TEST_USERNAME, TEST_HASHED_PASSWORD, TEST_ROLE_LIST);
		when(userRepository.findByUsernameAndHashedPassword(TEST_USERNAME, TEST_HASHED_PASSWORD)).thenReturn(Optional.of(user));
		when(tokenService.generateTokenFor(TEST_USERNAME, TEST_ROLE_LIST)).thenReturn(TEST_VALID_TOKEN);
		serviceUnderTest.authenticate(TEST_USERNAME, TEST_PASSWORD);
		verify(userRepository).findByUsernameAndHashedPassword(TEST_USERNAME, TEST_HASHED_PASSWORD);
	}
	
	@Test
	public void shouldDelegateOnTokenServiceToGenerateToken() throws Exception {
		var user = new User(TEST_USERNAME, TEST_HASHED_PASSWORD, TEST_ROLE_LIST);
		when(userRepository.findByUsernameAndHashedPassword(TEST_USERNAME, TEST_HASHED_PASSWORD)).thenReturn(Optional.of(user));
		when(tokenService.generateTokenFor(TEST_USERNAME, TEST_ROLE_LIST)).thenReturn(TEST_VALID_TOKEN);
		String resultingToken = serviceUnderTest.authenticate(TEST_USERNAME, TEST_PASSWORD);
		assertEquals(TEST_VALID_TOKEN, resultingToken);
		verify(tokenService).generateTokenFor(TEST_USERNAME, TEST_ROLE_LIST);
	}
	
	@Test
	public void shouldRetrieveUserAndRolesFromValidToken() throws Exception {
		when(tokenService.getTokenClaims(TEST_VALID_TOKEN)).thenReturn(Map.of(
				TokenService.USERNAME_CLAIM_KEY, TEST_USERNAME,
				TokenService.ROLE_LIST_CLAIM_KEY, TEST_ROLE_LIST));
		var user = serviceUnderTest.getUserDetailsFromToken(TEST_VALID_TOKEN);
		assertEquals(TEST_USERNAME, user.getUsername());
		assertEquals(TEST_ROLE_LIST, user.getRoles());
		verify(tokenService).getTokenClaims(TEST_VALID_TOKEN);
	}
	
	@Test
	public void shouldThrowBadCredentialsIfUserAndPasswordDontExist() {
		when(userRepository.findByUsernameAndHashedPassword(anyString(), anyString())).thenReturn(Optional.empty());
		assertThrows(BadCredentialsException.class, () -> {
			serviceUnderTest.authenticate("wrong_username", "bad_password");
		});
		verify(tokenService, never()).generateTokenFor(anyString(), anyString());
	}
}
