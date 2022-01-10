package com.w2m.challenge.superhero.auth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import com.w2m.challenge.superhero.model.auth.User;
import com.w2m.challenge.superhero.service.UserAuthService;
import com.w2m.challenge.superhero.test.helpers.AuthTokenTestHelper;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
public class JwtTokenAuthenticationProviderTest implements AuthTokenTestHelper {

	private JwtTokenAuthenticationProvider providerUnderTest;
	
	@Mock
	private UserAuthService mockUserAuthService;
	
	@BeforeEach
	public void setUp() {
		providerUnderTest = new JwtTokenAuthenticationProvider(mockUserAuthService);
		var user = new User();
		user.setUsername(TEST_USERNAME);
		user.setRoles(TEST_ROLE_LIST);
		when(mockUserAuthService.getUserDetailsFromToken(TEST_VALID_TOKEN)).thenReturn(user);
	}
	
	@Test
	public void shouldRetrieveUserDetailsFromToken() {
		var authentication = new UsernamePasswordAuthenticationToken(TEST_VALID_TOKEN, TEST_VALID_TOKEN);
		var userDetails = providerUnderTest.retrieveUser(TEST_VALID_TOKEN, authentication);
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
