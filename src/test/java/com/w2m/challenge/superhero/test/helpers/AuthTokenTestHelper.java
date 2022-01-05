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
		ObjectMapper om = new ObjectMapper();
		AuthTokenDTO responseDTO = om.readValue(responseString, AuthTokenDTO.class);
		JWSObject parsedJWT = JWSObject.parse(responseDTO.getToken());
		return Optional.ofNullable(parsedJWT.getHeader())
				.map(header -> header.getType() == JOSEObjectType.JWT)
				.orElse(false);
	}
}
