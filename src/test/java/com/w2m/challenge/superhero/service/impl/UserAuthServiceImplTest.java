package com.w2m.challenge.superhero.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.text.ParseException;
import java.util.Optional;

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
