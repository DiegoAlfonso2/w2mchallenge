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
	
	@Mock
	UserRepository userRepository;
	
	@BeforeEach
	public void setUp() {
		serviceUnderTest = new UserAuthServiceImpl(userRepository);
	}

	@Test
	public void shouldReturnTokenWithValidCredentials() throws JsonMappingException, JsonProcessingException, ParseException {
		var username = "pepito";
		var password = "m3g4h4x0r";
		var hashedPassword = DigestUtils.sha256Hex(username + password);
		var user = new User(username, hashedPassword, "ROLE_USER");
		Mockito.when(userRepository.findByUsernameAndHashedPassword(username, hashedPassword)).thenReturn(Optional.of(user));
		String resultingToken = serviceUnderTest.authenticate(username, password);
		assertTrue(isValidJWTString(resultingToken));
	}
}
