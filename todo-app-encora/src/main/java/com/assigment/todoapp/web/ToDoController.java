package com.assigment.todoapp.web;

import java.net.URI;
import java.time.Duration;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

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
    public ResponseEntity<ToDoItem> updateFlag(@PathVariable UUID id,@RequestBody ToDoItem todoItem) {
        ToDoItem updatedItem = todoService.updateFlag(id, todoItem);
        return ResponseEntity.ok(updatedItem);
    }
	
	@GetMapping("/api/todos/colorFlags")
	public ResponseEntity<List<Map<String, Object>>> fetchToDoItemsWithFlags() {
	    // Fetch all ToDo items
	    List<ToDoItem> toDoItems = todoService.fetchAllItems();

	    // Create a list to hold the ToDo items with flags
	    List<Map<String, Object>> toDoItemFlags = new ArrayList<>();

	    for (ToDoItem item : toDoItems) {
	        Map<String, Object> itemFlag = new HashMap<>();
	        itemFlag.put("item", item);

	        // Set the flag based on the difference in weeks
	        if (item.getDueDate() == null) {
	            itemFlag.put("flag", 0);
	        } else {
	            // Calculate the difference in weeks between the due date and today
	            long weeksBetween = ChronoUnit.WEEKS.between(LocalDate.now(), item.getDueDate());

	            if (weeksBetween <= 1) {
	                itemFlag.put("flag", 1);
	            } else if (weeksBetween <= 2) {
	                itemFlag.put("flag", 2);
	            } else {
	                itemFlag.put("flag", 3);
	            }
	        }

	        toDoItemFlags.add(itemFlag);
	    }

	    return ResponseEntity.ok(toDoItemFlags);
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
