package br.com.klimber.inova.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.klimber.inova.model.Customer;
import br.com.klimber.inova.repository.CustomerRepository;

@Service
public class CustomerService implements UserDetailsService {

	@Value("${customer.admin.username}")
	private String adminUsername;
	@Value("${customer.admin.password}")
	private String adminPassword;
	@Value("${customer.admin.email}")
	private String adminEmail;
	@Value("${customer.admin.firstname}")
	private String adminFirstName;
	@Value("${customer.admin.lastname}")
	private String adminLastName;
	@Autowired
	private CustomerRepository customerRepository;
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		if (customerRepository.count() == 0) {
			Customer admin = Customer.builder() //
					.email(adminEmail) //
					.username(adminUsername) //
					.password(passwordEncoder.encode(adminPassword)) //
					.role("ROLE_ADMIN") //
					.firstName(adminFirstName) //
					.lastName(adminLastName).build(); //
			customerRepository.saveAndFlush(admin);
		}
		return customerRepository.findByUsernameIgnoreCase(username);
	}

}
