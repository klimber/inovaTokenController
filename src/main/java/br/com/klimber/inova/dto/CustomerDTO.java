package br.com.klimber.inova.dto;

import java.util.Set;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerDTO {

	private Long id;

	private String username;

	private String email;

	private String fullName;

	private String extraInfo;

	private Set<String> authorities;

}
