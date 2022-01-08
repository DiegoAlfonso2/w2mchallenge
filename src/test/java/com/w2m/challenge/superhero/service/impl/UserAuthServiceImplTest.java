package com.w2m.challenge.superhero.service.impl;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.text.ParseException;
import java.util.Optional;

import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.w2m.challenge.superhero.model.auth.User;
import com.w2m.challenge.superhero.repository.UserRepository;
import com.w2m.challenge.superhero.test.helpers.AuthTokenTestHelper;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
public class UserAuthServiceImplTest implements AuthTokenTestHelper {

	private UserAuthServiceImpl serviceUnderTest;
	private final String USERNAME = "pepito";
	private final String PASSWORD = "m3g4h4x0r";
	private final String HASHED_PASSWORD = DigestUtils.sha256Hex(USERNAME + PASSWORD);
	
	@Mock
	UserRepository userRepository;
	
	@BeforeEach
	public void setUp() {
		serviceUnderTest = new UserAuthServiceImpl(userRepository);
		var user = new User(USERNAME, HASHED_PASSWORD, "ROLE_USER");
		Mockito.when(userRepository.findByUsernameAndHashedPassword(USERNAME, HASHED_PASSWORD)).thenReturn(Optional.of(user));
	}

	@Test
	public void shouldReturnTokenWithValidCredentials() throws JsonMappingException, JsonProcessingException, ParseException {
		String resultingToken = serviceUnderTest.authenticate(USERNAME, PASSWORD);
		assertTrue(isValidJWTString(resultingToken));
	}
	
	@Test
	public void shouldReturnRoleWithProperCredentials() throws Exception {
		String resultingToken = serviceUnderTest.authenticate(USERNAME, PASSWORD);
		assertTrue(hasClaim(resultingToken, "username", "pepito"));
		assertTrue(hasClaim(resultingToken, "roles", "ROLE_USER"));
	}
}
