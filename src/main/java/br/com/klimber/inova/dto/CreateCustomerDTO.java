package br.com.klimber.inova.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class CreateCustomerDTO {

	private String username;
	@NotBlank
	private String fullName;
	@NotBlank
	private String extraInfo;
	@Email
	@NotBlank
	private String email;
	@NotBlank
	private String password;
	@NotBlank
	private Long profileId;

}
