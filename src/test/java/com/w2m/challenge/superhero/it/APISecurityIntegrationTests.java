package com.w2m.challenge.superhero.it;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class APISecurityIntegrationTests {

	@Autowired
	private MockMvc mockMvc;
	
	@Test
	public void shouldFailWithHTTP401IfNotAuthenticated() throws Exception {
		mockMvc
			.perform(get("/heroes"))
			.andDo(print())
			.andExpect(status().isUnauthorized());
	}
}
