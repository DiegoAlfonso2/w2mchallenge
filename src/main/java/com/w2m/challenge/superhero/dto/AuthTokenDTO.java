package com.w2m.challenge.superhero.dto;

import lombok.Data;

@Data
public class AuthTokenDTO {

	private String token;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
}
