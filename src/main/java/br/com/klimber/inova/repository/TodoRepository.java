package br.com.klimber.inova.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.klimber.inova.model.Customer;
import br.com.klimber.inova.model.Todo;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Integer> {

	List<Todo> findByCustomer(Customer customer);

	List<Todo> findByCustomerAndCompleted(Customer customer, Boolean completed);

}
