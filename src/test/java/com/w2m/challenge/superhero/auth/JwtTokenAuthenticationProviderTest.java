package com.w2m.challenge.superhero.auth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import com.w2m.challenge.superhero.test.helpers.AuthTokenTestHelper;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
public class JwtTokenAuthenticationProviderTest implements AuthTokenTestHelper {

	private JwtTokenAuthenticationProvider providerUnderTest;
	
	@BeforeEach
	public void setUp() {
		this.providerUnderTest = new JwtTokenAuthenticationProvider();
	}
	
	@Test
	public void shouldRetrieveUserDetailsFromToken() {
		var authentication = new UsernamePasswordAuthenticationToken(TEST_USERNAME, TEST_VALID_TOKEN);
		var userDetails = providerUnderTest.retrieveUser(TEST_USERNAME, authentication);
		assertThat(userDetails.getUsername(), is("pepito"));
		assertThat(
				userDetails
						.getAuthorities()
						.stream()
						.map(grantedAuthority -> grantedAuthority.getAuthority())
						.anyMatch(s -> "ROLE_USER".equalsIgnoreCase(s)), 
				is(true));
	}
}
