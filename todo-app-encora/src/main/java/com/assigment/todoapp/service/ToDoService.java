package com.assigment.todoapp.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
	


	public Map<String, Object> paginateToDoItems(
	            List<ToDoItem> todoItems,
	            int page,
	            int pageSize) {

	        int totalItems = todoItems.size();
	        int totalPages = (int) Math.ceil((double) totalItems / pageSize);
	        int startIndex = (page - 1) * pageSize;
	        int endIndex = Math.min(startIndex + pageSize, totalItems);

	        List<ToDoItem> paginatedItems = todoItems.subList(startIndex, endIndex);
	        int itemsOnPage = paginatedItems.size();

	        Map<String, Object> response = new HashMap<>();
	        response.put("items", paginatedItems);
	        response.put("currentPage", page);
	        response.put("totalItems", totalItems);
	        response.put("totalPages", totalPages);
	        response.put("itemsOnPage", itemsOnPage);

	        return response;
	}
	
	public List<ToDoItem> sortToDoItems(
            List<ToDoItem> todoItems,
            String sortBy1,
            String order1,
            String sortBy2,
            String order2) {

        Collections.sort(todoItems, new Comparator<ToDoItem>() {
            @Override
            public int compare(ToDoItem t1, ToDoItem t2) {
                Map<String, Integer> priorityValues = new HashMap<>();
                priorityValues.put("Low", 1);
                priorityValues.put("Medium", 2);
                priorityValues.put("High", 3);

                int comparison1 = 0;
                if (sortBy1.equals("priority")) {
                    int priority1Value = priorityValues.getOrDefault(t1.getPriority(), 0);
                    int priority2Value = priorityValues.getOrDefault(t2.getPriority(), 0);
                    if (order1.equals("desc")) {
                        priority1Value = 4 - priority1Value;
                        priority2Value = 4 - priority2Value;
                    }
                    comparison1 = Integer.compare(priority1Value, priority2Value);
                } else if (sortBy1.equals("dueDate")) {
                    if (t1.getDueDate() == null && t2.getDueDate() == null) {
                        comparison1 = 0;
                    } else if (t1.getDueDate() == null) {
                        comparison1 = -1;
                    } else if (t2.getDueDate() == null) {
                        comparison1 = 1;
                    } else {
                        comparison1 = t1.getDueDate().compareTo(t2.getDueDate());
                    }
                    if (order1.equals("desc")) {
                        comparison1 = -comparison1;
                    }
                }

                if (comparison1 != 0) {
                    return comparison1;
                } else {
                    int comparison2 = 0;
                    if (sortBy2.equals("priority")) {
                        int priority1Value = priorityValues.getOrDefault(t1.getPriority(), 0);
                        int priority2Value = priorityValues.getOrDefault(t2.getPriority(), 0);
                        if (order2.equals("desc")) {
                            priority1Value = 4 - priority1Value;
                            priority2Value = 4 - priority2Value;
                        }
                        comparison2 = Integer.compare(priority1Value, priority2Value);
                    } else if (sortBy2.equals("dueDate")) {
                        if (t1.getDueDate() == null && t2.getDueDate() == null) {
                            comparison2 = 0;
                        } else if (t1.getDueDate() == null) {
                            comparison2 = -1;
                        } else if (t2.getDueDate() == null) {
                            comparison2 = 1;
                        } else {
                            comparison2 = t1.getDueDate().compareTo(t2.getDueDate());
                        }
                        if (order2.equals("desc")) {
                            comparison2 = -comparison2;
                        }
                    }
                    return comparison2;
                }
            }
        });

        return todoItems;
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
