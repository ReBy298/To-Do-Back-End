package com.assigment.todoapp.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.assigment.todoapp.domain.ToDoItem;
import com.assigment.todoapp.repository.ToDoRepository;

@Service
public class ToDoService {
	
	@Autowired //Inject 
	private ToDoRepository todoRepository;
	private List<ToDoItem> todoItems;
	public boolean aux;
	
	public ToDoService(ToDoRepository todoRepository) {
        this.todoItems = todoRepository.fetchAllToDoItems();
    }
	
	public List<ToDoItem> fetchAllToDoItems (String state, String name, String priority) {
	   
	    Stream<ToDoItem> stream = todoRepository.fetchAllToDoItems().stream();

	   
	    if (!state.equals("All")) {
	
	        if(state.equals("Done")) {
	        	aux = true;
	        }else {
	        	aux = false;
	        }
	        stream = stream.filter(item -> item.isDone() == aux);
	    }

	   
	    if (!priority.equals("All")) {
	        stream = stream.filter(item -> item.getPriority().equals(priority));
	    }

	   
	    if (!name.equals("")) {
	        stream = stream.filter(item -> item.getName().contains(name));
	    }

	    return stream.collect(Collectors.toList());
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
