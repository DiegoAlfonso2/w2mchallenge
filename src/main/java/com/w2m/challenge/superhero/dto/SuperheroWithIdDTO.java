package com.w2m.challenge.superhero.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class SuperheroWithIdDTO extends SuperheroDTO {

	private long id;
}
