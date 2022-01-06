package com.w2m.challenge.superhero.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.w2m.challenge.superhero.model.auth.User;

public interface UserRepository extends CrudRepository<User, String> {

	Optional<User> findByUsernameAndHashedPassword(String username, String hashedPassword);

	
}
