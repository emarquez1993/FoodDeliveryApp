package com.cogent.fooddeliveryapp.payload.request;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignInRequest {
	@NotBlank
	private String userName;
	@NotBlank
	private String password;
}
