package com.cogent.fooddeliveryapp.payload.request;

import java.time.LocalDate;
import java.util.Set;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.cogent.fooddeliveryapp.dto.Role;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignupRequest {
	
	@NotBlank
	private String email;
	@NotBlank
	private String name;
	@NotBlank
	private String password;
	@NotEmpty
	private Set<AddressRequest> addressRequest;
	@NotNull
	@JsonFormat(pattern = "MM-dd-yyyy")
	private LocalDate doj;
	@NotEmpty
	private Set<String> roles;
}
