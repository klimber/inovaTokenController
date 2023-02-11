package br.com.klimber.inova.service;

import br.com.klimber.inova.model.CustomerProfile;
import java.util.List;

import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.klimber.inova.model.Customer;
import br.com.klimber.inova.repository.CustomerRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CustomerService implements UserDetailsService {

	private final CustomerProfileService profileService;
	private final CustomerRepository customerRepository;

	public CustomerService(CustomerRepository customerRepository, PasswordEncoder encoder,
						   CustomerProfileService profileService,
						   @Value("${inova.admin.username}") String adminUsername,
						   @Value("${inova.admin.password}") String adminPassword,
						   @Value("${inova.admin.email}") String adminEmail,
						   @Value("${inova.admin.fullname}") String adminFullName,
						   @Value("${inova.admin.extraInfo}") String adminExtraInfo) {
		this.customerRepository = customerRepository;
		this.profileService = profileService;
		if (customerRepository.count() == 0) {
			CustomerProfile adminProfile = new CustomerProfile("Admin Profile", Set.of("ROLE_ADMIN"));
			adminProfile = profileService.save(adminProfile);
			Customer admin = Customer.builder() //
									 .email(adminEmail) //
									 .username(adminUsername) //
									 .password(encoder.encode(adminPassword)) //
									 .profile(adminProfile)
									 .fullName(adminFullName) //
									 .extraInfo(adminExtraInfo).build(); //
			this.save(admin);
		}
	}

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Customer customer = customerRepository.findByUsernameIgnoreCase(username)
				.orElseThrow(() -> new UsernameNotFoundException("No user found with username " + username));
		// need to load authorities inside transaction
		customer.getAuthorities().size();
		return customer;
	}

	public List<Customer> findAll(Example<Customer> example) {
		return customerRepository.findAll(example);
	}

	public Customer save(Customer customer) {
		return customerRepository.save(customer);
	}

	public Customer findByUsername(String username) {
		return customerRepository.findByUsernameIgnoreCase(username)
				.orElseThrow(() -> new UsernameNotFoundException("No user found with username " + username));
	}

	public Customer findById(Long id) {
		return customerRepository.findById(id).get();
	}

	public long count() {
		return customerRepository.count();
	}

	public void addExtraAuthority(Long customerId, String authority) {
		Customer customer = findById(customerId);
		customer.getExtraAuthorities().add(authority);
		save(customer);
	}

	public void removeExtraAuthority(Long customerId, String authority) {
		Customer customer = findById(customerId);
		customer.getExtraAuthorities().remove(authority);
		save(customer);
	}

}
