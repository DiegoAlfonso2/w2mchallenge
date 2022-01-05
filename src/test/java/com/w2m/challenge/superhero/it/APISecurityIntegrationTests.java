package com.w2m.challenge.superhero.it;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.Charset;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.w2m.challenge.superhero.test.helpers.AuthTokenTestHelper;

@SpringBootTest
@AutoConfigureMockMvc
public class APISecurityIntegrationTests implements AuthTokenTestHelper {
	public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

	@Autowired
	private MockMvc mockMvc;
	
	@Test
	public void shouldFailWithHTTP401IfNotAuthenticated() throws Exception {
		mockMvc
			.perform(get("/heroes"))
			.andDo(print())
			.andExpect(status().isUnauthorized());
	}
	
	@Test
	public void shouldReturnLoginTokenWithRightCredentials() throws Exception {
		StringBuilder credentialsBuilder = new StringBuilder();
		credentialsBuilder.append("{ \n");
		credentialsBuilder.append("    \"username\": \"pepito\", \n");
		credentialsBuilder.append("    \"password\": \"m3g4h4x0r\" \n");
		credentialsBuilder.append("}");
		MvcResult result = mockMvc
			.perform(post("/login")
					.contentType(APPLICATION_JSON_UTF8)
					.content(credentialsBuilder.toString()))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON_UTF8))
			.andReturn();
		assertTrue(isValidJWTLoginResponse(result.getResponse().getContentAsString()));
	}
}
