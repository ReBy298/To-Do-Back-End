package com.assigment.todoapp.web;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.assigment.todoapp.domain.ToDoItem;
import com.assigment.todoapp.service.ToDoService;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@CrossOrigin(origins = "http://localhost:8080/")
public class ToDoController {

	private final ToDoService todoService;

	public ToDoController(ToDoService todoService) {
		this.todoService = todoService;
	}

	@GetMapping("/api/todos")
	public ResponseEntity<?> fetchAllToDoItems(
			@RequestParam(defaultValue = "All") String state,
			@RequestParam(defaultValue = "") String name,
			@RequestParam(defaultValue = "All") String priority,
			@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "10") int pageSize,
			@RequestParam(defaultValue = "dueDate") String sortBy1,
			@RequestParam(defaultValue = "asc") String order1,
			@RequestParam(defaultValue = "priority") String sortBy2,
			@RequestParam(defaultValue = "asc") String order2) {

		List<ToDoItem> todoItems = todoService.fetchAllToDoItems(state, name, priority);
		List<ToDoItem> sortedItems = todoService.sortToDoItems(todoItems, sortBy1, order1, sortBy2, order2);
		Map<String, Object> response = todoService.paginateToDoItems(sortedItems, page, pageSize);

		return ResponseEntity.ok(response);
	}

	@PostMapping("/api/todos")
	public ResponseEntity<ToDoItem> createToDoItem(@RequestBody ToDoItem todoItem) {
		if (todoItem == null || todoItem.getName() == null || todoItem.getName().isEmpty()) {
			return ResponseEntity.badRequest().body(null);
		}
		ToDoItem createdToDoItem = todoService.createToDoItem(todoItem);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(createdToDoItem.getId())
				.toUri();
		return ResponseEntity.created(location).body(createdToDoItem);
	}

	@PostMapping("/api/todos/{id}/done")
	public ResponseEntity<ToDoItem> updateFlag(@PathVariable UUID id, @RequestBody ToDoItem todoItem) {
		ToDoItem updatedItem = todoService.updateFlag(id, todoItem);
		return ResponseEntity.ok(updatedItem);
	}

	@GetMapping("/api/todos/colorFlags")
	public ResponseEntity<List<Map<String, Object>>> fetchToDoItemsWithFlags() {
		List<Map<String, Object>> toDoItemFlags = todoService.fetchToDoItemsWithFlags();
		return ResponseEntity.ok(toDoItemFlags);
	}

	@GetMapping("/api/todos/averageTime")
	public ResponseEntity<?> fetchAverageCompletionTime() {
		Map<String, Object> response = todoService.fetchAverageCompletionTimes();
		return ResponseEntity.ok(response);
	}

	@PutMapping("/api/todos/{id}")
	public ResponseEntity<ToDoItem> updateToDoItem(@PathVariable UUID id, @RequestBody ToDoItem updatedToDoItem) {
		if (id == null || updatedToDoItem == null || updatedToDoItem.getName() == null || updatedToDoItem.getName().isEmpty()) {
			return ResponseEntity.badRequest().body(null);
		}
		ToDoItem updatedItem = todoService.updateToDoItem(id, updatedToDoItem);
		return ResponseEntity.ok(updatedItem);
	}

	@PutMapping("/api/todos/{id}/undone")
	public ResponseEntity<ToDoItem> updateFlag2(@PathVariable UUID id, @RequestBody ToDoItem updatedToDoItem) {
		ToDoItem updatedItem = todoService.updateFlag(id, updatedToDoItem);
		return ResponseEntity.ok(updatedItem);
	}

	@DeleteMapping("/api/todos/{id}")
	public ResponseEntity<Void> deleteToDoItem(@PathVariable UUID id) {
		todoService.deleteToDoItem(id);
		return ResponseEntity.noContent().build();
	}
}