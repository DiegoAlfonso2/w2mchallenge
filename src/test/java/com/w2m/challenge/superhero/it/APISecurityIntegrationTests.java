package com.w2m.challenge.superhero.it;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
		MvcResult result = mockMvc
			.perform(post("/login")
					.contentType(APPLICATION_JSON_UTF8)
					.content(createJsonForLogin(Optional.of(TEST_USERNAME), Optional.of(TEST_PASSWORD))))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON_UTF8))
			.andReturn();
		assertTrue(isValidJWTLoginResponse(result.getResponse().getContentAsString()));
	}
	
	@Test
	public void shouldAcceptRequestIfAuthenticated() throws Exception {
		MvcResult loginResult = mockMvc
			.perform(post("/login")
					.contentType(APPLICATION_JSON_UTF8)
					.content(createJsonForLogin(Optional.of(TEST_USERNAME), Optional.of(TEST_PASSWORD))))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON_UTF8))
			.andReturn();
		var token = new JSONObject(loginResult.getResponse().getContentAsString()).getString("token");
		mockMvc
			.perform(get("/heroes")
					.contentType(APPLICATION_JSON_UTF8)
					.header("Authorization", "Bearer " + token))
			.andDo(print())
			.andExpect(status().isOk());
	}
	
	@Test
	public void shouldReturnBadRequestIfLoginLacksUsername() throws Exception {
		mockMvc
				.perform(post("/login")
						.contentType(APPLICATION_JSON_UTF8)
						.content(createJsonForLogin(Optional.empty(), Optional.of(TEST_PASSWORD))))
				.andDo(print())
				.andExpect(status().isBadRequest())
				.andExpect(content().contentTypeCompatibleWith(APPLICATION_JSON_UTF8));
	}
	
	private String createJsonForLogin(Optional<String> username, Optional<String> password) throws JsonProcessingException {
		Map<String, String> elements = new HashMap<String, String>();
		if (username.isPresent())
			elements.put("username", username.get());
		if (password.isPresent())
			elements.put("password", password.get());
		return new ObjectMapper().writeValueAsString(elements);
	}
}
