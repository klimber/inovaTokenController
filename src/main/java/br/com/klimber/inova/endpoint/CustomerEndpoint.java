package br.com.klimber.inova.endpoint;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import br.com.klimber.inova.dto.ChangePasswordDTO;
import br.com.klimber.inova.dto.CustomerDTO;
import br.com.klimber.inova.dto.PatchCustomerDTOAdmin;
import br.com.klimber.inova.mapper.CustomerMapper;
import br.com.klimber.inova.model.Customer;
import br.com.klimber.inova.service.CustomerService;

@RestController
public class CustomerEndpoint {

	@Autowired
	private CustomerService customerService;

	@Autowired
	private CustomerMapper customerMapper;

	@Autowired
	private PasswordEncoder passwordEncoder;

	/**
	 * Searches for customers using example. This search is CASE SENSITIVE. All
	 * parameters are optional.
	 * 
	 * @param email
	 * @param fullName
	 * @param extraInfo
	 * @return A List of CustomerDTO
	 */
	@Secured("ROLE_ADMIN")
	@GetMapping("/customers")
	public List<CustomerDTO> listCustomers( //
			@RequestParam(required = false) String email, //
			@RequestParam(required = false) String fullName, //
			@RequestParam(required = false) String extraInfo //
	) {
		Example<Customer> example = Example
				.of(Customer.builder().email(email).fullName(fullName).extraInfo(extraInfo).build());
		return customerService.findAll(example).stream().map(customer -> customerMapper.toDTO(customer))
				.collect(Collectors.toList());
	}

	@Secured("ROLE_ADMIN")
	@PostMapping("/customers")
	public CustomerDTO addCustomer(@RequestBody Customer customer) {
		customer.setId(null);
		customer.setAuthorities(Set.of("ROLE_USER"));
		customer.setPassword(passwordEncoder.encode(customer.getPassword()));
		return customerMapper.toDTO(customerService.save(customer));
	}

	@Secured("ROLE_ADMIN")
	@GetMapping("/customer")
	public CustomerDTO findByUsername(@RequestParam String username) {
		return customerMapper.toDTO(customerService.findByUsername(username));
	}

	@Secured("ROLE_ADMIN")
	@GetMapping("/customers/{id}")
	public CustomerDTO findById(@PathVariable("id") Long id) {
		return customerMapper.toDTO(customerService.findById(id));
	}

	@RequestMapping(method = RequestMethod.OPTIONS, path = "/customers")
	public ResponseEntity<Object> options() {
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@GetMapping("/customers/whoAmI")
	public CustomerDTO whoAmI() {
		return customerMapper.toDTO((Customer) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
	}

	@Secured("ROLE_ADMIN")
	@PostMapping("/customers/{userId}/authority/{authority}")
	public boolean addAuthority(@PathVariable Long userId, @PathVariable String authority) {
		Customer customer = customerService.findById(userId);
		customer.addAuthority(authority);
		customerService.save(customer);
		return true;
	}

	@Secured("ROLE_ADMIN")
	@DeleteMapping("/customers/{userId}/authority/{authority}")
	public boolean removeAuthority(@PathVariable Long userId, @PathVariable String authority) {
		Customer customer = customerService.findById(userId);
		customer.removeAuthority(authority);
		customerService.save(customer);
		return true;
	}

	@PatchMapping("/changePassword")
	public boolean changePassword(@RequestBody ChangePasswordDTO passwordDTO) {
		Customer customer = (Customer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (passwordEncoder.matches(passwordDTO.getOldPassword(), customer.getPassword())) {
			customer.setPassword(passwordEncoder.encode(passwordDTO.getNewPassword()));
			customerMapper.toDTO(customerService.save(customer));
			return true;
		}
		return false;
	}

	@Secured("ROLE_ADMIN")
	@PatchMapping("/customer/{id}")
	public CustomerDTO patchUserAdmin(@RequestBody PatchCustomerDTOAdmin customerPatch, @PathVariable("id") Long id) {
		Customer customer = customerService.findById(id);
		customer.setUsername(customerPatch.getUsername());
		customer.setEmail(customerPatch.getEmail());
		customer.setExtraInfo(customerPatch.getExtraInfo());
		customer.setFullName(customerPatch.getFullName());
		if (!StringUtils.isEmpty(customerPatch.getPassword())) {
			customer.setPassword(passwordEncoder.encode(customerPatch.getPassword()));
		}
		return customerMapper.toDTO(customerService.save(customer));
	}

}
