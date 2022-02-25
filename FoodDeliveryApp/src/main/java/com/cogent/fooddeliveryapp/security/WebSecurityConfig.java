package com.cogent.fooddeliveryapp.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.cogent.fooddeliveryapp.security.jwt.AuthEntryPointJwt;
import com.cogent.fooddeliveryapp.security.jwt.AuthTokenFilter;
import com.cogent.fooddeliveryapp.security.service.UserDetailsServiceImpl;
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	UserDetailsServiceImpl userDetailsServiceImpl;
	@Autowired
	private AuthEntryPointJwt unauthorizedHandler;
	
	
	@Bean
	public AuthTokenFilter authenticationJwtTokenFilter()
	{
		return new AuthTokenFilter();
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		// TODO Auto-generated method stub
		auth.userDetailsService(userDetailsServiceImpl).passwordEncoder(passwordEncoder());
	}
	
	@Bean
	public PasswordEncoder passwordEncoder()
	{
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		// TODO Auto-generated method stub
		return super.authenticationManagerBean();
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// core part of the security
		//we can restrict the access of endpoints with this config method
		//we can set unauthorized access
		//we can provide direct go access to signup/signin (authorizing the res)
		//applying token validation for end points
		//CORS:
		// TODO Auto-generated method stub
		http.cors()
		.and()
		.csrf().disable().exceptionHandling().authenticationEntryPoint(unauthorizedHandler)
		.and()
		.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		.and()
		.authorizeRequests()
			.antMatchers("/auth/**").permitAll()
			.antMatchers("/food/**").authenticated().anyRequest().permitAll();
		
		http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
	}
}
