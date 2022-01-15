package com.w2m.challenge.superhero.dto;

import java.util.List;
import java.util.Optional;

import lombok.Data;

@Data
public class SuperheroDTO {

	private String heroName;
	private Optional<String> secretOrCivilianIdentity;
	private boolean hasCape;
	private List<String> superpowers;
	
}
