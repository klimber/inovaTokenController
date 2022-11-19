package br.com.klimber.inova.endpoint;

import br.com.klimber.inova.model.Customer;
import br.com.klimber.inova.model.Todo;
import br.com.klimber.inova.repository.TodoRepository;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequiredArgsConstructor
public class TodoEndpoint {

	private final TodoRepository todoRepository;

	@GetMapping("/todoApp/todos")
	@Transactional
	public List<Todo> getTodos() {
		Customer customer = (Customer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<Todo> todos = todoRepository.findByCustomer(customer);
		if (!todos.isEmpty())
			todos.get(0).getCustomer().getAuthorities().size();
		return todos;
	}

	@PostMapping("/todoApp/todos")
	public Todo addTodo(@RequestBody Todo todo) {
		Customer customer = (Customer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		todo.setCustomer(customer);
		return todoRepository.save(todo);
	}

	@PatchMapping("/todoApp/todos/{id}")
	public Todo updateTodo(@PathVariable("id") Integer id, @RequestBody Todo todo) {
		Customer customer = (Customer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Optional<Todo> currentTodo = todoRepository.findById(id);
		if (currentTodo.isEmpty() || currentTodo.get().getCustomer().getId() != customer.getId()) {
			throw new RuntimeException("Todo does not exist or is not owned by user");
		}
		todo.setId(id);
		todo.setCustomer(customer);
		return todoRepository.save(todo);
	}

	@DeleteMapping("/todoApp/todos/{id}")
	public ResponseEntity<Object> deleteTodo(@PathVariable Integer id) {
		Customer customer = (Customer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Optional<Todo> currentTodo = todoRepository.findById(id);
		if (currentTodo.isEmpty() || currentTodo.get().getCustomer().getId() != customer.getId()) {
			throw new RuntimeException("Todo does not exist or is not owned by user");
		}
		todoRepository.deleteById(id);
		return new ResponseEntity<Object>(HttpStatus.OK);
	}

	@PatchMapping("/todoApp/checkAllTodos")
	public ResponseEntity<Object> checkAll(@RequestBody JsonNode completed) {
		Customer customer = (Customer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<Todo> allTodos = todoRepository.findByCustomerAndCompleted(customer,
				!completed.get("completed").asBoolean());
		allTodos.forEach(todo -> todo.setCompleted(completed.get("completed").asBoolean()));
		todoRepository.saveAll(allTodos);
		return new ResponseEntity<Object>(HttpStatus.OK);
	}

	@DeleteMapping("/todoApp/todosClearCompleted")
	public ResponseEntity<Object> todosClearCompleted() {
		Customer customer = (Customer) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		List<Todo> completedTodos = todoRepository.findByCustomerAndCompleted(customer, true);
		todoRepository.deleteAll(completedTodos);
		return new ResponseEntity<Object>(HttpStatus.OK);
	}

	/**
	 * This method initializes 2 sample todos in the database
	 */
	@GetMapping("/todoApp/initTodos")
	public void initTodos() {
		List<Todo> listaInit = new ArrayList<>();
		Customer customer = Customer.builder().id(1L).build();
		listaInit.add(new Todo("From Database", false, customer));
		listaInit.add(new Todo("Another One", false, customer));
		todoRepository.saveAll(listaInit);
	}

	/**
	 * TODO This is a fake endpoint for logout, logout is not yet supported
	 * 
	 * @return HTTP OK
	 */
	@DeleteMapping("/todoApp/token")
	public ResponseEntity<Object> logout() {
		return new ResponseEntity<Object>(HttpStatus.OK);
	}
}
