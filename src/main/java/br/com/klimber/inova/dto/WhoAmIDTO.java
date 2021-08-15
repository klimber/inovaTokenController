package br.com.klimber.inova.dto;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;

import br.com.klimber.inova.model.Customer;
import lombok.Data;

@Data
public class WhoAmIDTO {

	private Long id;

	private String username;

	private String email;

	private String fullName;

	private String extraInfo;

	private Long profileId;

	private Set<String> authorities;

	public WhoAmIDTO(Customer customer) {
		this.id = customer.getId();
		this.username = customer.getUsername();
		this.email = customer.getEmail();
		this.fullName = customer.getFullName();
		this.extraInfo = customer.getExtraInfo();
		this.profileId = customer.getProfile().getId();
		this.authorities = customer.getAuthorities().stream().map(GrantedAuthority::getAuthority)
				.collect(Collectors.toSet());
	}
}
