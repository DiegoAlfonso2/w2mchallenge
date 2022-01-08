package com.w2m.challenge.superhero.test.helpers;

import java.text.ParseException;
import java.util.Optional;

import org.apache.commons.codec.digest.DigestUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.JWSObject;
import com.w2m.challenge.superhero.dto.AuthTokenDTO;

public interface AuthTokenTestHelper {
	
	String TEST_USERNAME = "pepito";
	String TEST_PASSWORD = "m3g4h4x0r";
	String TEST_HASHED_PASSWORD = DigestUtils.sha256Hex(TEST_USERNAME + TEST_PASSWORD);
	String TEST_ROLE_LIST = "ROLE_USER";
	String TEST_VALID_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6InBlcGl0byIsInJvbGVzIjoiUk9MRV9VU0VSIn0.-G8xKxvmDaOknbDjFuu_8__Sx1la5YsufiSn6H2mGD0";


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
	
	default boolean hasClaim(String token, String claim, String content) throws ParseException {
		JWSObject parsedJWT = JWSObject.parse(token);
		return Optional.ofNullable(parsedJWT.getPayload())
				.map(payload -> payload.toJSONObject())
				.flatMap(claimsMap -> Optional.ofNullable(claimsMap.get(claim)))
				.map(value -> value.equals(content))
				.orElse(false);
	}
	
	private String parseLoginJSONResponseToGetToken(String jsonString) throws JsonMappingException, JsonProcessingException, ParseException {
		ObjectMapper om = new ObjectMapper();
		AuthTokenDTO responseDTO = om.readValue(jsonString, AuthTokenDTO.class);
		return responseDTO.getToken();
	}
}
