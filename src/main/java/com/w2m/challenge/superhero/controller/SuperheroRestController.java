package com.w2m.challenge.superhero.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.w2m.challenge.superhero.dto.SuperheroWithIdDTO;

@RestController
@RequestMapping("/heroes")
public class SuperheroRestController {

	@GetMapping
	public ResponseEntity<List<SuperheroWithIdDTO> > getHeroes() {
		return new ResponseEntity<List<SuperheroWithIdDTO>>(Collections.emptyList(), HttpStatus.OK);
	}
}
