package com.cogent.fooddeliveryapp.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cogent.fooddeliveryapp.dto.Address;
import com.cogent.fooddeliveryapp.dto.Role;
import com.cogent.fooddeliveryapp.dto.User;
import com.cogent.fooddeliveryapp.enums.ERole;
import com.cogent.fooddeliveryapp.exception.IdNotFoundException;
import com.cogent.fooddeliveryapp.payload.request.SignInRequest;
import com.cogent.fooddeliveryapp.payload.request.SignupRequest;
import com.cogent.fooddeliveryapp.payload.response.JwtResponse;
import com.cogent.fooddeliveryapp.repository.RoleRepository;
import com.cogent.fooddeliveryapp.security.jwt.JwtUtils;
import com.cogent.fooddeliveryapp.security.service.UserDetailsImpl;
import com.cogent.fooddeliveryapp.service.UserService;
@RestController
@RequestMapping("/auth")
public class AuthController {
	
	@Autowired
	UserService userService;
	@Autowired
	RoleRepository roleRepository;
	@Autowired
	AuthenticationManager authenticationManager;
	@Autowired
	PasswordEncoder passwordEncoder;
	@Autowired
	JwtUtils jwtUtils;
	
	
	@PostMapping("/register")
	public ResponseEntity<?> createUser(@Valid @RequestBody SignupRequest signupRequest) {
		Set<Role> roles = new HashSet<>();
		
		if(signupRequest.getRoles() == null)
		{
			Role userRole = roleRepository.findByRoleName(ERole.ROLE_USER)
					.orElseThrow(()-> new IdNotFoundException("role not found"));
					roles.add(userRole);
		}
		else {
			signupRequest.getRoles().forEach(e->{
				switch (e) {
				case "user":
					Role userRole = roleRepository.findByRoleName(ERole.ROLE_USER)
					.orElseThrow(()-> new IdNotFoundException("role not found"));
					roles.add(userRole);
					break;
				case "admin":
					Role adminRole = roleRepository.findByRoleName(ERole.ROLE_ADMIN)
					.orElseThrow(()-> new IdNotFoundException("role not found"));
					roles.add(adminRole);
					break;
				default:
					break;
				} 
			});
		}
		
		User user = new User();
		
		Set<Address> addresses = new HashSet<>();
		signupRequest.getAddressRequest().forEach(e->{
			Address address =  new Address();
			address.setCity(e.getCity());
			address.setCountry(e.getCountry());
			address.setHouseNo(e.getHouseNo());
			address.setState(e.getState());
			address.setStreet(e.getStreet());
			address.setUser(user);
			address.setZip(e.getZip());
			addresses.add(address);
		});
		
		user.setAddresses(addresses);
		user.setDoj(signupRequest.getDoj());
		user.setEmail(signupRequest.getEmail());
		user.setUserName(signupRequest.getUserName());
		user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
		user.setRoles(roles);
		
		User user2 = userService.addUser(user);
		
		return ResponseEntity.status(201).body(user2);
	}
	
	@PostMapping("/signin")
	public ResponseEntity<?> signInUser(@Valid @RequestBody SignInRequest signInRequest)
	{
		Authentication authentication = 
				authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(signInRequest.getUserName(), signInRequest.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtUtils.generateToken(authentication);
		
		UserDetailsImpl userDetailsImpl = (UserDetailsImpl) authentication.getPrincipal();
		List<String> roles = userDetailsImpl.getAuthorities()
				.stream().map(e-> e.getAuthority())
				.collect(Collectors.toList());
		
		return ResponseEntity.ok(new JwtResponse(jwt, userDetailsImpl.getId(), userDetailsImpl.getUsername(), userDetailsImpl.getEmail(), roles));
	}
}
