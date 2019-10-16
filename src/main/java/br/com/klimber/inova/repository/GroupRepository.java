package br.com.klimber.inova.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.klimber.inova.model.Group;

@Repository
public interface GroupRepository extends JpaRepository<Group, String> {

}
