package com.assigment.todoapp.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
	private List<ToDoItem> todoItems = new ArrayList<>();
	public boolean aux;
	
	public ToDoService(ToDoRepository todoRepository) {
        this.todoItems = todoRepository.fetchAllToDoItems();
    }
	
    public void setTodoItems(List<ToDoItem> todoItems) {
        this.todoItems = todoItems;
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
        todoItem.setCreationDate(LocalDateTime.now());
        todoItem.setDone(false);

        
        todoItems.add(todoItem);
        return todoItem;
    }
	
	public ToDoItem updateFlag(UUID id, ToDoItem todoItem) {
		Optional<ToDoItem> existingToDoItem = todoItems.stream()
	            .filter(item -> item.getId().equals(id))
	            .findFirst();

	    if (existingToDoItem.isEmpty()) {
	        throw new RuntimeException("ToDoItem no encontrado");
	    }
	    ToDoItem existingItem = existingToDoItem.get();
	    
	    if(existingItem.isDone()== false) {
	    	existingItem.setDone(todoItem.isDone()); 
		    existingItem.setDoneDate(LocalDateTime.now());
		    return existingItem;
	    }else {
	    	existingItem.setDone(todoItem.isDone()); 
		    existingItem.setDoneDate(null);
		    return existingItem;
	    }
	    
	    
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
	    existingItem.setDueDate(updatedToDoItem.getDueDate());
	    return existingItem;
	}

	public void deleteToDoItem(UUID id) {
		todoItems.removeIf(item -> item.getId().equals(id));
	}

	public List<ToDoItem> fetchAllDoneToDoItems(String priority) {
        List<ToDoItem> doneItems = todoItems.stream()
            .filter(item -> item.isDone() && (priority.equals("All") || item.getPriority().equals(priority)))
            .collect(Collectors.toList());

        return doneItems;
    }

	public List<ToDoItem> fetchAllItems() {
		List<ToDoItem> doneItems = todoItems.stream().collect(Collectors.toList());

	    return doneItems;
	}

	
	
}
