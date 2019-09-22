package br.com.klimber.inova.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.klimber.inova.model.Customer;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

	public List<Customer> findByFirstNameIgnoreCase(String firstName);

	public List<Customer> findByLastNameIgnoreCase(String lastName);

	public Optional<Customer> findByUserNameIgnoreCase(String userName);

	public List<Customer> findByEmailIgnoreCase(String email);
}
