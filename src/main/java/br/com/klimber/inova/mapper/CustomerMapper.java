package br.com.klimber.inova.mapper;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.springframework.security.core.GrantedAuthority;

import br.com.klimber.inova.dto.CustomerDTO;
import br.com.klimber.inova.model.Customer;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

	public CustomerDTO toDTO(Customer customer);

	public static Set<String> map(Collection<? extends GrantedAuthority> authorities) {
		return authorities.stream().map(authority -> authority.getAuthority()).collect(Collectors.toSet());
	}

}
