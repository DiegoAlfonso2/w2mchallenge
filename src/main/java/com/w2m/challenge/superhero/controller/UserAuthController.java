package com.w2m.challenge.superhero.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.w2m.challenge.superhero.dto.AuthTokenDTO;
import com.w2m.challenge.superhero.dto.UserDTO;
import com.w2m.challenge.superhero.service.UserAuthService;

@RestController
public class UserAuthController {
	
	private UserAuthService userAuthService;

	@Autowired
	public UserAuthController(UserAuthService userAuthService) {
		this.userAuthService = userAuthService;
	}

	// TODO perform input validation (e.g. username not empty) to avoid hitting the DB
	@PostMapping(path = "/login", produces = {MediaType.APPLICATION_JSON_VALUE})
	public ResponseEntity<AuthTokenDTO> login(@Valid @RequestBody UserDTO user) {
		var token = userAuthService.authenticate(user.getUsername(), user.getPassword());
		AuthTokenDTO result = new AuthTokenDTO();
		result.setToken(token);
		return ResponseEntity.ok(result);
	}
}
