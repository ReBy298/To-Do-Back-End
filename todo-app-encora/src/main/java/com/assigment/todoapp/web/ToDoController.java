package com.assigment.todoapp.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.assigment.todoapp.domain.ToDoItem;
import com.assigment.todoapp.service.ToDoService;

@RestController
@CrossOrigin(origins = "http://localhost:3000/")
public class ToDoController {
	
	
	
	
	private final ToDoService todoService;
	
	public ToDoController(ToDoService todoService){
		this.todoService = todoService;
		
	}
	
	
	
	@GetMapping("/api/todos")
	public ResponseEntity<?> fetchAllToDoItems(
			@RequestParam(defaultValue = "All") String state,
	        @RequestParam(defaultValue = "") String name,
	        @RequestParam(defaultValue = "All") String priority,
	        @RequestParam(defaultValue = "1") int page,
	        @RequestParam(defaultValue = "10") int pageSize) {

	    List<ToDoItem> todoItems = todoService.fetchAllToDoItems(state, name, priority);

	    // Implement pagination
	    int totalItems = todoItems.size();
	    int totalPages = (int) Math.ceil((double) totalItems / pageSize);
	    int startIndex = (page - 1) * pageSize;
	    int endIndex = Math.min(startIndex + pageSize, totalItems);

	    List<ToDoItem> paginatedItems = todoItems.subList(startIndex, endIndex);
	    
	    int itemsOnPage = paginatedItems.size();

	 // Create a map to hold both items and pagination info
	    Map<String, Object> response = new HashMap<>();
	    response.put("items", paginatedItems);
	    response.put("currentPage", page);
	    response.put("totalItems", totalItems);
	    response.put("totalPages", totalPages);
	    response.put("itemsOnPage", itemsOnPage);
	    

	    return ResponseEntity.ok(response);
	}
	
	
	
	@PostMapping("/api/todos")
    public ResponseEntity<ToDoItem> createToDoItem(@RequestBody ToDoItem todoItem) {
        ToDoItem createdToDoItem = todoService.createToDoItem(todoItem);
        return ResponseEntity.ok(createdToDoItem);
    }
	
	@PostMapping("/api/todos/{id}/done")
    public ResponseEntity<ToDoItem> updateFlag(@PathVariable UUID id,@RequestBody ToDoItem todoItem) {
        ToDoItem updatedItem = todoService.updateFlag(id, todoItem);
        return ResponseEntity.ok(updatedItem);
    }
	
	@PutMapping("/api/todos/{id}")
    public ResponseEntity<ToDoItem> updateToDoItem(@PathVariable UUID id, @RequestBody ToDoItem updatedToDoItem) {
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
