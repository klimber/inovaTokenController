package br.com.klimber.inova.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.domain.Example;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.klimber.inova.model.Customer;
import br.com.klimber.inova.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerService implements UserDetailsService {

	private final CustomerRepository customerRepository;

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
