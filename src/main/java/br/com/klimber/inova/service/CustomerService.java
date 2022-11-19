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
	private final String adminUsername;
	private final String adminPassword;
	private final String adminEmail;
	private final String adminFullName;
	private final String adminExtraInfo;
	private final String profile;

	public CustomerService(CustomerRepository customerRepository, PasswordEncoder encoder,
						   CustomerProfileService profileService,
						   @Value("${customer.admin.username}") String adminUsername,
						   @Value("${customer.admin.password}") String adminPassword,
						   @Value("${customer.admin.email}") String adminEmail,
						   @Value("${customer.admin.fullname}") String adminFullName,
						   @Value("${customer.admin.extraInfo}") String adminExtraInfo,
						   @Value("${spring.profiles.active}") String profile) {
		this.customerRepository = customerRepository;
		this.profileService = profileService;
		this.adminUsername = adminUsername;
		this.adminPassword = adminPassword;
		this.adminEmail = adminEmail;
		this.adminFullName = adminFullName;
		this.adminExtraInfo = adminExtraInfo;
		this.profile = profile;
		if (customerRepository.count() == 0) {
			CustomerProfile initProfile = new CustomerProfile("Admin Profile", Set.of("ROLE_ADMIN"));
			initProfile = profileService.save(initProfile);
			Customer admin = Customer.builder() //
									 .email(this.adminEmail) //
									 .username(this.adminUsername) //
									 .password(encoder.encode(this.adminPassword)) //
									 .profile(initProfile)
									 .fullName(this.adminFullName) //
									 .extraInfo(this.adminExtraInfo).build(); //
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
