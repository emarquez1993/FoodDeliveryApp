package com.cogent.fooddeliveryapp.payload.response;

import java.time.LocalDate;
import java.util.Set;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.cogent.fooddeliveryapp.payload.request.AddressRequest;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
	@NotBlank
	private String email;
	@NotBlank
	private String name;
	//@NotEmpty
	private Set<AddressRequest> addressRequest;
	@NotNull
	@JsonFormat(pattern = "MM-dd-yyyy")
	private LocalDate doj;
	@NotEmpty
	private Set<String> roles;
}
