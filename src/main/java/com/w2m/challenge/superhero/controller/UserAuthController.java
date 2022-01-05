package com.w2m.challenge.superhero.controller;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.w2m.challenge.superhero.dto.AuthTokenDTO;
import com.w2m.challenge.superhero.dto.UserDTO;

@RestController
public class UserAuthController {

	@PostMapping(path = "/login", produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<AuthTokenDTO> login(@RequestBody UserDTO user) {
		AuthTokenDTO result = new AuthTokenDTO();
		result.setToken("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c");
		return ResponseEntity.ok(result);
	}
}
