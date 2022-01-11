package com.w2m.challenge.superhero.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class UserDTO {
	@NotBlank(message = "username is mandatory")
	private String username;
	@NotNull(message = "password is mandatory")
	private String password;
}
