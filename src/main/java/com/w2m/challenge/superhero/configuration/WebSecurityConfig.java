package com.w2m.challenge.superhero.configuration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AnonymousAuthenticationFilter;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import com.w2m.challenge.superhero.auth.JwtTokenAuthenticationFilter;
import com.w2m.challenge.superhero.auth.JwtTokenAuthenticationProvider;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	private static final RequestMatcher SECURE_URLS = new OrRequestMatcher(
			new AntPathRequestMatcher("/heroes/**")
	);

	private JwtTokenAuthenticationProvider authProvider;
	
	public WebSecurityConfig(JwtTokenAuthenticationProvider authProvider) {
		this.authProvider = authProvider;
	}

	@Override
	public void configure(HttpSecurity http) throws Exception {
		
		http.csrf().disable()
			.authorizeRequests()
				.antMatchers("/login").permitAll()
				.requestMatchers(SECURE_URLS).authenticated()
			.and()
			.exceptionHandling()
				.defaultAuthenticationEntryPointFor(unauthorizedEntryPoint(), SECURE_URLS)
			.and()
			.authenticationProvider(authProvider)
			.addFilterBefore(jwtTokenAuthenticationFilter(), AnonymousAuthenticationFilter.class);
	}
	
	@Bean
	public AuthenticationEntryPoint unauthorizedEntryPoint() {
		return new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED);
	}
	
	@Bean
	public JwtTokenAuthenticationFilter jwtTokenAuthenticationFilter() throws Exception {
		JwtTokenAuthenticationFilter filter = new JwtTokenAuthenticationFilter(SECURE_URLS, authenticationManager());
		SimpleUrlAuthenticationSuccessHandler successHandler = new SimpleUrlAuthenticationSuccessHandler();
		successHandler.setRedirectStrategy((HttpServletRequest request, HttpServletResponse response, String url) -> {});
		filter.setAuthenticationSuccessHandler(successHandler);
		return filter;
	}
}
