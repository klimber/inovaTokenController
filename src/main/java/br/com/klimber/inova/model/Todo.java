package br.com.klimber.inova.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class Todo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	private String title;
	private Boolean completed = false;
	@ManyToOne
	private Customer customer;

	public Todo(String title, Boolean completed, Customer customer) {
		this.title = title;
		this.completed = completed;
		this.customer = customer;
	}
}
