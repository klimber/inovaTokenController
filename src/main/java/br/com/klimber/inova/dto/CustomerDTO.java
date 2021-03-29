package br.com.klimber.inova.dto;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;

import br.com.klimber.inova.model.Customer;
import lombok.Data;

@Data
public class CustomerDTO {

	private Long id;

	private String username;

	private String email;

	private String fullName;

	private String extraInfo;

	private Set<String> authorities;

	public CustomerDTO(Customer customer) {
		this.id = customer.getId();
		this.username = customer.getUsername();
		this.email = customer.getEmail();
		this.fullName = customer.getFullName();
		this.extraInfo = customer.getExtraInfo();
		this.authorities = customer.getAuthorities().stream().map(GrantedAuthority::getAuthority)
				.collect(Collectors.toSet());
	}

}
