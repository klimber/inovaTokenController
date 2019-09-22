package br.com.klimber.inova.endpoint;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.klimber.inova.dto.CustomerDTO;
import br.com.klimber.inova.mapper.CustomerMapper;
import br.com.klimber.inova.model.Customer;
import br.com.klimber.inova.repository.CustomerRepository;

@RestController
public class CustomerEndpoint {

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private CustomerMapper customerMapper;

	/**
	 * Searches for customers using example. This search is CASE SENSITIVE.
	 */
	@GetMapping("/customers")
	public List<CustomerDTO> listCustomers( //
			@RequestParam(required = false) String email, //
			@RequestParam(required = false) String firstName, //
			@RequestParam(required = false) String lastName //
	) {
		Example<Customer> example = Example
				.of(Customer.builder().email(email).firstName(firstName).lastName(lastName).build());
		return customerRepository.findAll(example).stream().map(c -> customerMapper.toDTO(c))
				.collect(Collectors.toList());
	}

	@PostMapping("/customer")
	public CustomerDTO addCustomer(@RequestBody Customer customer) {
		customer.setId(null);
		return customerMapper.toDTO(customerRepository.save(customer));
	}

	@GetMapping("/customer")
	public CustomerDTO findByUserName(@RequestParam String userName) {
		return customerMapper.toDTO(customerRepository.findByUserNameIgnoreCase(userName).get());
	}

	@GetMapping("/customer/{id}")
	public CustomerDTO findById(@PathVariable("id") Long id) {
		return customerMapper.toDTO(customerRepository.findById(id).get());
	}
}
