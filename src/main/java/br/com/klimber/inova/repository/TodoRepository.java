package br.com.klimber.inova.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.klimber.inova.model.Todo;

@Repository
public interface TodoRepository extends JpaRepository<Todo, Integer> {

}
