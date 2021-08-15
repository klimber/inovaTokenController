package br.com.klimber.inova.endpoint;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Example;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import br.com.klimber.inova.dto.*;
import br.com.klimber.inova.model.Customer;
import br.com.klimber.inova.model.CustomerProfile;
import br.com.klimber.inova.service.CustomerProfileService;
import br.com.klimber.inova.service.CustomerService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class CustomerEndpoint {

	private final CustomerService customerService;
	private final CustomerProfileService profileService;
	private final PasswordEncoder passwordEncoder;

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
		return customerService.findAll(example).stream().map(CustomerDTO::new).collect(Collectors.toList());
	}

	@Secured("ROLE_ADMIN")
	@PostMapping("/customers")
	public CustomerDTO addCustomer(@RequestBody CreateCustomerDTO customerDTO) {
		CustomerProfile profile = profileService.findById(customerDTO.getProfileId());
		Customer customer = Customer.builder()
				.id(null)
				.username(customerDTO.getUsername())
				.fullName(customerDTO.getFullName())
				.extraInfo(customerDTO.getExtraInfo())
				.email(customerDTO.getEmail())
				.password(passwordEncoder.encode(customerDTO.getPassword()))
				.profile(profile)
				.build();
		return new CustomerDTO(customerService.save(customer));
	}

	@Secured("ROLE_ADMIN")
	@GetMapping("/customer")
	public CustomerDTO findByUsername(@RequestParam String username) {
		return new CustomerDTO(customerService.findByUsername(username));
	}

	@Secured("ROLE_ADMIN")
	@GetMapping("/customers/{id}")
	public CustomerDTO findById(@PathVariable("id") Long id) {
		return new CustomerDTO(customerService.findById(id));
	}

	@GetMapping("/customers/whoAmI")
	public WhoAmIDTO whoAmI() {
		return new WhoAmIDTO((Customer) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
	}

	@PatchMapping("/changePassword")
	public boolean changePassword(@RequestBody ChangePasswordDTO passwordDTO) {
		Customer customer = (Customer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (passwordEncoder.matches(passwordDTO.getOldPassword(), customer.getPassword())) {
			customer.setPassword(passwordEncoder.encode(passwordDTO.getNewPassword()));
			customerService.save(customer);
			return true;
		}
		return false;
	}

	@Secured("ROLE_ADMIN")
	@PatchMapping("/customer/{id}")
	public CustomerDTO patchUserAdmin(@RequestBody PatchCustomerDTOAdmin customerPatch, @PathVariable("id") Long id) {
		CustomerProfile profile = profileService.findById(customerPatch.getProfileId());
		Customer customer = customerService.findById(id);
		customer.setUsername(customerPatch.getUsername());
		customer.setEmail(customerPatch.getEmail());
		customer.setExtraInfo(customerPatch.getExtraInfo());
		customer.setFullName(customerPatch.getFullName());
		customer.setProfile(profile);
		if (!StringUtils.isEmpty(customerPatch.getPassword())) {
			customer.setPassword(passwordEncoder.encode(customerPatch.getPassword()));
		}
		return new CustomerDTO(customerService.save(customer));
	}

	@Secured("ROLE_ADMIN")
	@PostMapping("/customer/{customerId}/authority/{authority}")
	public void addExtraAuthority(@PathVariable Long customerId, @PathVariable String authority) {
		customerService.addExtraAuthority(customerId, authority);
	}

	@Secured("ROLE_ADMIN")
	@DeleteMapping("/customer/{customerId}/authority/{authority}")
	public void removeExtraAuthority(@PathVariable Long customerId, @PathVariable String authority) {
		customerService.removeExtraAuthority(customerId, authority);
	}

}
