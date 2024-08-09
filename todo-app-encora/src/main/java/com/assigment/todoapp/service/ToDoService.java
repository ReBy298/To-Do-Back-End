package com.assigment.todoapp.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.assigment.todoapp.domain.ToDoItem;
import com.assigment.todoapp.repository.ToDoRepository;

@Service
public class ToDoService {
	
	@Autowired //Inject 
	private ToDoRepository todoRepository;
	private List<ToDoItem> todoItems;
	
	public ToDoService(ToDoRepository todoRepository) {
        this.todoItems = todoRepository.fetchAllToDoItems();
    }
	
	public List<ToDoItem> fetchAllToDoItems (Boolean done, String name, String priority) {
		
		// Modify the query based on the provided parameters
        if (done != null) {
            // Filter by done/undone
        	
            return todoRepository.findByDone(done);
        } else if (priority != null) {
            // Filter by priority
            return todoRepository.findByPriority(priority);
        }else if (name != null) {
            // Filter by name or part of the name
            return todoRepository.findByNameContainingIgnoreCase(name);
        }  else {
            // Default query (no filters)
        	return todoRepository.fetchAllToDoItems();
        }
		
	}

	public ToDoItem createToDoItem(ToDoItem todoItem) {
        todoItem.setId(UUID.randomUUID());
        todoItem.setCreationDate(LocalDate.now());
        todoItems.add(todoItem);
        return todoItem;
    }

	public ToDoItem updateToDoItem(UUID id, ToDoItem updatedToDoItem) {
		Optional<ToDoItem> existingToDoItem = todoItems.stream()
	            .filter(item -> item.getId().equals(id))
	            .findFirst();

	    if (existingToDoItem.isEmpty()) {
	        throw new RuntimeException("ToDoItem no encontrado");
	    }

	    ToDoItem existingItem = existingToDoItem.get();
	    existingItem.setName(updatedToDoItem.getName()); 
	    existingItem.setDone(updatedToDoItem.isDone()); 
	    existingItem.setPriority(updatedToDoItem.getPriority());
	    return existingItem;
	}

	public void deleteToDoItem(UUID id) {
		todoItems.removeIf(item -> item.getId().equals(id));
	}

	
	
}
