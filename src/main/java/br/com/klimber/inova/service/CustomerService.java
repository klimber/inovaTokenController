package br.com.klimber.inova.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.klimber.inova.model.Customer;
import br.com.klimber.inova.repository.CustomerRepository;

@Service
public class CustomerService implements UserDetailsService {

	@Autowired
	private CustomerRepository customerRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return customerRepository.findByUsernameIgnoreCase(username);
	}

	public List<Customer> findAll(Example<Customer> example) {
		return customerRepository.findAll(example);
	}

	public Customer save(Customer customer) {
		return customerRepository.save(customer);
	}

	public Customer findByUsername(String username) {
		return customerRepository.findByUsernameIgnoreCase(username);
	}

	public Customer findById(Long id) {
		return customerRepository.findById(id).get();
	}

}
