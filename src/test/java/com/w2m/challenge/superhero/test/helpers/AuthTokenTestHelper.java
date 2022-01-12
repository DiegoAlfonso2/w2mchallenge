package com.w2m.challenge.superhero.test.helpers;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;
import java.util.function.Function;

import org.apache.commons.codec.digest.DigestUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.JOSEObjectType;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.w2m.challenge.superhero.dto.AuthTokenDTO;

public interface AuthTokenTestHelper {
	
	String TEST_USERNAME = "pepito";
	String TEST_PASSWORD = "m3g4h4x0r";
	String TEST_HASHED_PASSWORD = DigestUtils.sha256Hex(TEST_USERNAME + TEST_PASSWORD);
	String TEST_ROLE_LIST = "ROLE_USER";
	String TEST_VALID_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VybmFtZSI6InBlcGl0byIsInJvbGVzIjoiUk9MRV9VU0VSIn0.-G8xKxvmDaOknbDjFuu_8__Sx1la5YsufiSn6H2mGD0";
	long TEST_EXPIRATION_60_SECONDS = 60L;
	String TEST_SECRET = "secret";


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
	
	default String buildTestJwt(String secret, LocalDateTime issuedAt, LocalDateTime expiration, Optional<String> username, Optional<String> roles) throws Exception {
		Function<LocalDateTime, Date> localDateTimeToDate = (LocalDateTime time) -> Date.from(time.atZone(ZoneId.systemDefault()).toInstant());
		
		JWTClaimsSet.Builder claimsBuilder = new JWTClaimsSet.Builder()
				.issueTime(localDateTimeToDate.apply(issuedAt))
				.expirationTime(localDateTimeToDate.apply(expiration));

		if (roles.isPresent()) {
			claimsBuilder = claimsBuilder.claim("roles", roles.get());
		}
		
		if (username.isPresent()) {
			claimsBuilder = claimsBuilder.claim("username", username.get());
		}
		var claims = claimsBuilder.build();
		
		JWSSigner signer = new MACSigner(secret.getBytes());
		SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256, JOSEObjectType.JWT, null, null, null, null, null, null, null, null, null, true, null, null), claims);
		signedJWT.sign(signer);
		return signedJWT.serialize();
	}

}
