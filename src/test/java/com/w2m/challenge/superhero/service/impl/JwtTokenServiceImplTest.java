package com.w2m.challenge.superhero.service.impl;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.text.ParseException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.w2m.challenge.superhero.test.helpers.AuthTokenTestHelper;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
public class JwtTokenServiceImplTest implements AuthTokenTestHelper {

	private JwtTokenServiceImpl serviceUnderTest;
	
	@BeforeEach
	public void setUp() {
		this.serviceUnderTest = new JwtTokenServiceImpl();
	}
	
	@Test
	public void shouldGenerateValidJwtToken() throws ParseException {
		String token = serviceUnderTest.generateTokenFor(TEST_USERNAME, TEST_ROLE_LIST);
		assertTrue(isValidJWTString(token));
	}

	@Test 
 	public void shouldGenerateJwtTokenWithProvidedClaims() throws ParseException {
		String token = serviceUnderTest.generateTokenFor(TEST_USERNAME, TEST_ROLE_LIST);
		assertTrue(hasClaim(token, "username", TEST_USERNAME));
		assertTrue(hasClaim(token, "roles", TEST_ROLE_LIST));
	}
}