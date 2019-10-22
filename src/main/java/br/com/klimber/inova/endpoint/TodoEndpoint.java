package br.com.klimber.inova.endpoint;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;

import br.com.klimber.inova.model.Todo;
import br.com.klimber.inova.repository.TodoRepository;

@RestController
public class TodoEndpoint {

	@Autowired
	private TodoRepository todoRepository;

	@CrossOrigin
	@GetMapping("/todos")
	public List<Todo> getTodos() {
		return todoRepository.findAll();
	}

	@CrossOrigin
	@PostMapping("/todos")
	public Todo addTodo(@RequestBody Todo todo) {
		return todoRepository.save(todo);
	}

	@CrossOrigin
	@PatchMapping("/todos/{id}")
	public Todo updateTodo(@PathVariable("id") Integer id, @RequestBody Todo todo) {
		todo.setId(id);
		return todoRepository.save(todo);
	}

	@CrossOrigin
	@DeleteMapping("/todos/{id}")
	public String deleteTodo(@PathVariable Integer id) {
		todoRepository.deleteById(id);
		return "Success";
	}

	@CrossOrigin
	@PatchMapping("/checkAllTodos")
	public String checkAll(@RequestBody JsonNode completed) {
		List<Todo> allTodos = todoRepository.findAll();
		allTodos.forEach(todo -> todo.setCompleted(completed.get("completed").asBoolean()));
		todoRepository.saveAll(allTodos);
		return "Success";
	}

	@CrossOrigin
	@DeleteMapping("/todosClearCompleted")
	public String todosClearCompleted(@RequestBody JsonNode data) {
		data.get("todos").forEach(id -> todoRepository.deleteById(id.asInt()));
		return "Success";
	}

	@GetMapping("/initTodos")
	public void initTodos() {
		List<Todo> listaInit = new ArrayList<>();
		listaInit.add(new Todo("From Database", false));
		listaInit.add(new Todo("Another One", false));
		todoRepository.saveAll(listaInit);
	}

}
