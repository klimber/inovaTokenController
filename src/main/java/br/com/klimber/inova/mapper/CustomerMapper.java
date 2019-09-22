package br.com.klimber.inova.mapper;

import org.mapstruct.Mapper;

import br.com.klimber.inova.dto.CustomerDTO;
import br.com.klimber.inova.model.Customer;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

	public CustomerDTO toDTO(Customer customer);

}
