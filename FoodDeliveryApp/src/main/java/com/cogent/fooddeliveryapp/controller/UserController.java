package com.cogent.fooddeliveryapp.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cogent.fooddeliveryapp.dto.Address;
import com.cogent.fooddeliveryapp.dto.Role;
import com.cogent.fooddeliveryapp.dto.User;
import com.cogent.fooddeliveryapp.enums.ERole;
import com.cogent.fooddeliveryapp.exception.IdNotFoundException;
import com.cogent.fooddeliveryapp.exception.NoDataFoundException;
import com.cogent.fooddeliveryapp.payload.request.AddressRequest;
import com.cogent.fooddeliveryapp.payload.request.SignupRequest;
import com.cogent.fooddeliveryapp.payload.request.UpdateRequest;
import com.cogent.fooddeliveryapp.payload.response.UserResponse;
import com.cogent.fooddeliveryapp.repository.RoleRepository;
import com.cogent.fooddeliveryapp.repository.UserRepository;
import com.cogent.fooddeliveryapp.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {
	@Autowired
	UserService userService;
	@Autowired
	RoleRepository roleRepository;
	
	@GetMapping("/getAll")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<?> getAllUsers() throws NoDataFoundException
	{
		List<User> list = userService.getAllUsers();
		List<UserResponse> userResponses = new ArrayList<UserResponse>();
		list.forEach(e->{
			UserResponse userResponse = new UserResponse();
			userResponse.setName(e.getUserName());
			userResponse.setEmail(e.getEmail());
			userResponse.setDoj(e.getDoj());
			Set<String> roles = new HashSet<String>();
			e.getRoles().forEach(e2->{
				roles.add(e2.getRoleName().name());
			});
			userResponse.setRoles(roles);
			Set<AddressRequest> addresses = new HashSet<AddressRequest>();
			e.getAddresses().forEach(e3->{
				AddressRequest addressRequest = new AddressRequest();
				addressRequest.setCity(e3.getCity());
				addressRequest.setCountry(e3.getCountry());
				addressRequest.setHouseNo(e3.getHouseNo());
				addressRequest.setState(e3.getState());
				addressRequest.setStreet(e3.getStreet());
				addressRequest.setZip(e3.getZip());
				addresses.add(addressRequest);
			});
			userResponse.setAddressRequest(addresses);
			userResponses.add(userResponse);
		});
		if(userResponses.size() > 0)
			return ResponseEntity.ok(userResponses);
		else
		{
			throw new NoDataFoundException("there is no data");
		}
	}
	
	@GetMapping(value = "/{id}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<?> getUserById(@PathVariable("id")long id) throws NoDataFoundException {
		
	User user =	userService.getUserById(id).orElseThrow(()->new NoDataFoundException("data not available"));
		// DTO ===> UserResponse()
	UserResponse userResponse=  new UserResponse();
	userResponse.setEmail(user.getEmail());
	userResponse.setName(user.getUserName());
	Set<String> roles= new HashSet<>();
	userResponse.setDoj(user.getDoj());
	user.getRoles().forEach(e2->{
		roles.add(e2.getRoleName().name());
	});
	Set<AddressRequest> addresses = new HashSet<>();
	user.getAddresses().forEach(e3->{
		AddressRequest address2 = new AddressRequest();
		address2.setHouseNo(e3.getHouseNo());
		address2.setCity(e3.getCity());
		address2.setCountry(e3.getCountry());
		address2.setState(e3.getState());
		address2.setStreet(e3.getStreet());
		address2.setZip(e3.getZip());
		addresses.add(address2);
	});
	userResponse.setAddressRequest(addresses);
	userResponse.setRoles(roles);
	return ResponseEntity.status(200).body(userResponse);
	
	}
	
	@DeleteMapping(value = "/:{id}")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<?> deleteUserById(@PathVariable("id")long id) throws NoDataFoundException
	{
		User user =	userService.getUserById(id).orElseThrow(()->new NoDataFoundException("User not found"));
		String del = userService.deleteUserById(id);
		Map<String, String> message = new HashMap<>();
		if(del.equals("success"))
		{
			message.put("message", "food record successfully deleted");
		}
		else
		{
			message.put("message", "delete aborted");
		}
		return ResponseEntity.status(200).body(message);
	}
	
	@PutMapping(value = "/update/{id}")
	@PreAuthorize("hasRole('ROLE_USER')")
	public ResponseEntity<?> updateUserById(@PathVariable("id")long id, @Valid @RequestBody UpdateRequest updateRequest) throws NoDataFoundException
	{
		User user =	userService.getUserById(id).orElseThrow(()->new NoDataFoundException("User not found"));
		user.setEmail(updateRequest.getEmail());
		user.setPassword(updateRequest.getPassword());
		Set<Address> set = new HashSet<Address>();
		updateRequest.getAddressRequest().forEach(e->{
			Address a = new Address();
			a.setCity(e.getCity());
			a.setCountry(e.getCountry());
			a.setHouseNo(e.getHouseNo());
			a.setState(e.getState());
			a.setStreet(e.getStreet());
			a.setZip(e.getZip());
			a.setUser(user);
			set.add(a);
		});
		user.setAddresses(set);
		User user2 = userService.updateUser(user);
		return ResponseEntity.status(201).body(user2);
	}
	
	
}