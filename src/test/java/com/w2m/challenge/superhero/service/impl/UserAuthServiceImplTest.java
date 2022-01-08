package com.w2m.challenge.superhero.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
import com.w2m.challenge.superhero.service.TokenService;
import com.w2m.challenge.superhero.test.helpers.AuthTokenTestHelper;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
public class UserAuthServiceImplTest implements AuthTokenTestHelper {

	private UserAuthServiceImpl serviceUnderTest;
	private final String USERNAME = "pepito";
	private final String PASSWORD = "m3g4h4x0r";
	private final String HASHED_PASSWORD = DigestUtils.sha256Hex(USERNAME + PASSWORD);
	private final String ROLE_LIST = "ROLE_USER";
	private final String VALID_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6InBlcGl0byIsInJvbGVzIjoiUk9MRV9VU0VSIn0.-G8xKxvmDaOknbDjFuu_8__Sx1la5YsufiSn6H2mGD0";
	
	@Mock
	UserRepository userRepository;
	
	@Mock
	TokenService tokenService;
	
	@BeforeEach
	public void setUp() {
		serviceUnderTest = new UserAuthServiceImpl(userRepository, tokenService);
		var user = new User(USERNAME, HASHED_PASSWORD, ROLE_LIST);
		Mockito.when(userRepository.findByUsernameAndHashedPassword(USERNAME, HASHED_PASSWORD)).thenReturn(Optional.of(user));
		Mockito.when(tokenService.generateTokenFor(USERNAME, ROLE_LIST)).thenReturn(VALID_TOKEN);
	}

	@Test
	public void shouldDelegateOnUserRepositoryToGetUserDetails() throws JsonMappingException, JsonProcessingException, ParseException {
		serviceUnderTest.authenticate(USERNAME, PASSWORD);
		Mockito.verify(userRepository).findByUsernameAndHashedPassword(USERNAME, HASHED_PASSWORD);
	}
	
	@Test
	public void shouldDelegateOnTokenServiceToGenerateToken() throws Exception {
		String resultingToken = serviceUnderTest.authenticate(USERNAME, PASSWORD);
		assertEquals(VALID_TOKEN, resultingToken);
		Mockito.verify(tokenService).generateTokenFor(USERNAME, ROLE_LIST);
	}
}
