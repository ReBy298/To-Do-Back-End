package com.assigment.todoapp.domain;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class ToDoItem {
    
    private UUID id = UUID.randomUUID();
    
    private String name;

    private LocalDateTime dueDate;

    private boolean done;

    private LocalDateTime doneDate;

    private String priority;
    
    
    private LocalDateTime creationDate;
    
    
	public UUID getId() {
		return id;
	}

	public void setId(UUID idCounter) {
		this.id = idCounter;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocalDateTime getDueDate() {
		return dueDate;
	}

	public void setDueDate(LocalDateTime dueDate) {
		this.dueDate = dueDate;
	}

	public boolean isDone() {
		return done;
	}

	public void setDone(boolean done) {
		this.done = done;
	}

	public LocalDateTime getDoneDate() {
		return doneDate;
	}

	public void setDoneDate(LocalDateTime doneDate) {
		this.doneDate = doneDate;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
	    List<String> validPriorities = Arrays.asList("Low", "Medium", "High");

	    if (validPriorities.contains(priority)) {
	        this.priority = priority;
	    } else {
	        throw new IllegalArgumentException("Invalid priority: " + priority);
	    }
	}

	public LocalDateTime getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(LocalDateTime creationDate) {
		this.creationDate = creationDate;
	}
	
}
