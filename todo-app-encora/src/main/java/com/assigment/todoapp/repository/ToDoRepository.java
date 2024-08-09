package com.assigment.todoapp.repository;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.assigment.todoapp.domain.ToDoItem;
					
@Repository
public class ToDoRepository  { // interface 
	

	private List<ToDoItem> todoItems;

    public ToDoRepository(List<ToDoItem> todoItems) {
        this.todoItems = todoItems;
    }
	
	public List<ToDoItem>  fetchAllToDoItems () {
		return todoItems;
	}

	public List<ToDoItem> findByDone(boolean aux) {
        return todoItems.stream()
                .filter(item -> item.isDone() == aux)
                .collect(Collectors.toList());
    }

    public List<ToDoItem> findByNameContainingIgnoreCase(String name) {
        return todoItems.stream()
                .filter(item -> item.getName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<ToDoItem> findByPriority(String priority) {
    	return todoItems.stream()
                .filter(item -> item.getPriority().equalsIgnoreCase(priority))
                .collect(Collectors.toList());
    }

}
