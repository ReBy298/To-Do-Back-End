package com.assigment.todoapp.web;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
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
@CrossOrigin(origins = "http://localhost:8080/")
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
	        @RequestParam(defaultValue = "10") int pageSize,
	        @RequestParam(defaultValue = "dueDate") String sortBy1, 
	        @RequestParam(defaultValue = "asc") String order1, 
	        @RequestParam(defaultValue = "priority") String sortBy2, 
	        @RequestParam(defaultValue = "asc") String order2) {

	    List<ToDoItem> todoItems = todoService.fetchAllToDoItems(state, name, priority);
	    
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
	                comparison1 = t1.getDueDate().compareTo(t2.getDueDate());
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
	                    comparison2 = t1.getDueDate().compareTo(t2.getDueDate());
	                    if (order2.equals("desc")) {
	                        comparison2 = -comparison2;
	                    }
	                }
	                return comparison2;
	            }
	        }
	    });


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
	@GetMapping("/api/todos/averageTime")
	public ResponseEntity<?> fetchAverageCompletionTime() {

	    // Fetch all done ToDo items
	    List<ToDoItem> doneItemsAll = todoService.fetchAllDoneToDoItems("All");
	    List<ToDoItem> doneItemsHigh = todoService.fetchAllDoneToDoItems("High");
	    List<ToDoItem> doneItemsMedium = todoService.fetchAllDoneToDoItems("Medium");
	    List<ToDoItem> doneItemsLow = todoService.fetchAllDoneToDoItems("Low");
	    
	  

	    // Calculate the average time between creation and done for all priorities
	    double averageDurationAll = calculateAverageDuration(doneItemsAll);
	    double averageDurationHigh = calculateAverageDuration(doneItemsHigh);
	    double averageDurationMedium = calculateAverageDuration(doneItemsMedium);
	    double averageDurationLow = calculateAverageDuration(doneItemsLow);

	    // Create a map to hold the average times
	    Map<String, Object> response = new HashMap<>();
	    response.put("averageTimeAll", averageDurationAll);
	    response.put("averageTimeHigh", averageDurationHigh);
	    response.put("averageTimeMedium", averageDurationMedium);
	    response.put("averageTimeLow", averageDurationLow);

	    return ResponseEntity.ok(response);
	}

	private double calculateAverageDuration(List<ToDoItem> doneItems) {
	    long totalDuration = 0;
	    for (ToDoItem item : doneItems) {
	        Duration duration = Duration.between(item.getCreationDate(), item.getDoneDate());
	        totalDuration += duration.toMillis();
	    }
	    return (double) totalDuration / doneItems.size();
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
