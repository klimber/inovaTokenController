package br.com.klimber.inova.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerDTO {

	private Long id;

	private String username;

	private String email;

	private String firstName;

	private String lastName;

	private String role;

}
