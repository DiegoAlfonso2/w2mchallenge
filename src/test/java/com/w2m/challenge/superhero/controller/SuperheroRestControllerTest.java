package com.w2m.challenge.superhero.controller;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.json.JSONArray;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(
		controllers = SuperheroRestController.class,
		excludeAutoConfiguration = SecurityAutoConfiguration.class,
		// I had to explicitly exclude Security Configuration because it depends on
		// JwtTokenAuthenticationProvider, which is not loaded by WebMvcTest.
		// That could be fixed with a mock provider, but it made no sense to include 
		// Security in an unit test anyway.
		excludeFilters = {@Filter(EnableWebSecurity.class)})
public class SuperheroRestControllerTest {

	@Autowired
	private MockMvc mockMvc;
	
	@Test
	public void shouldReturnEmptyListWhenThereAreNoHeroes() throws Exception {
		var result = mockMvc
				.perform(get("/heroes"))
				.andExpect(status().isOk())
				.andDo(print())
				.andReturn();
		JSONArray resJson = new JSONArray(result.getResponse().getContentAsString());
		assertTrue(resJson.length() == 0);
	}
}
