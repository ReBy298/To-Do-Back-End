package com.assigment.todoapp.web;

import java.util.List;
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
	
	@GetMapping("/api/todoItems")
	public ResponseEntity<?> fetchAllToDoItems(
            @RequestParam(required = false) Boolean done,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String priority ){
		
		
		List<ToDoItem> todoItems = todoService.fetchAllToDoItems(done, name, priority);
		
		//return ResponseEntity.ok(todoItems);
		return ResponseEntity.ok(todoItems);
		
	}
	
	@PostMapping("/api/todoItems")
    public ResponseEntity<ToDoItem> createToDoItem(@RequestBody ToDoItem todoItem) {
        ToDoItem createdToDoItem = todoService.createToDoItem(todoItem);
        return ResponseEntity.ok(createdToDoItem);
    }
	
	@PutMapping("/api/todoItems/{id}")
    public ResponseEntity<ToDoItem> updateToDoItem(@PathVariable UUID id, @RequestBody ToDoItem updatedToDoItem) {
        ToDoItem updatedItem = todoService.updateToDoItem(id, updatedToDoItem);
        return ResponseEntity.ok(updatedItem);
    }

    @DeleteMapping("/api/todoItems/{id}")
    public ResponseEntity<Void> deleteToDoItem(@PathVariable UUID id) {
        todoService.deleteToDoItem(id);
        return ResponseEntity.noContent().build();
    }
	
	
}
