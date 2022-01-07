package com.w2m.challenge.superhero.test.helpers;

import java.text.ParseException;
import java.util.Optional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.JWSObject;
import com.w2m.challenge.superhero.dto.AuthTokenDTO;

public interface AuthTokenTestHelper {

	default boolean isValidJWTLoginResponse(String responseString) throws JsonMappingException, JsonProcessingException, ParseException {
		String token = parseLoginJSONResponseToGetToken(responseString);
		return isValidJWTString(token);
	}
	
	default boolean isValidJWTString(String token) throws ParseException {
		JWSObject parsedJWT = JWSObject.parse(token);
		return Optional.ofNullable(parsedJWT.getHeader())
				.map(header -> header.getType().equals(JOSEObjectType.JWT))
				.orElse(false);		
	}
	
	private String parseLoginJSONResponseToGetToken(String jsonString) throws JsonMappingException, JsonProcessingException, ParseException {
		ObjectMapper om = new ObjectMapper();
		AuthTokenDTO responseDTO = om.readValue(jsonString, AuthTokenDTO.class);
		return responseDTO.getToken();
	}
}
