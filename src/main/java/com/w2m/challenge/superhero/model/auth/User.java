package com.w2m.challenge.superhero.model.auth;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="User")
public class User implements UserDetails {
	/**
	 * 
	 */
	private static final long serialVersionUID = -359457141949423776L;

	private final static String ROLE_SEPARATOR = ",";

	@Id
	private String username;
	private String hashedPassword;
	private String roles;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return Arrays.asList(roles.split(ROLE_SEPARATOR))
				.stream()
				.map(role -> new SimpleGrantedAuthority(role))
				.collect(Collectors.toList());
	}

	@Override
	public String getPassword() {
		return getHashedPassword();
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Currently account expiration is not supported
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Currently account locking is not supported
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Currently credentials expiration is not supported
		return true;
	}
	@Override
	public boolean isEnabled() {
		// TODO Currently account disabling is not supported
		return true;
	}
}
