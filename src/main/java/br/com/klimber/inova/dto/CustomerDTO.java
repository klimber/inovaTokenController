package br.com.klimber.inova.dto;

import java.util.Set;

import br.com.klimber.inova.model.Customer;
import lombok.Data;

@Data
public class CustomerDTO {

	private Long id;

	private String username;

	private String email;

	private String fullName;

	private String extraInfo;

	private Long profileId;

	private Set<String> extraAuthorities;

	public CustomerDTO(Customer customer) {
		this.id = customer.getId();
		this.username = customer.getUsername();
		this.email = customer.getEmail();
		this.fullName = customer.getFullName();
		this.extraInfo = customer.getExtraInfo();
		this.profileId = customer.getProfile().getId();
		this.extraAuthorities = customer.getExtraAuthorities();
	}

}
