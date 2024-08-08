package com.assigment.todoapp.domain;
import java.time.LocalDate;
import java.util.UUID;

public class ToDoItem {
    
    private UUID id = UUID.randomUUID();
    
    private String name;

    private LocalDate dueDate;

    private boolean done;

    private LocalDate doneDate;

    private String priority;
    
    
    
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

	public LocalDate getDueDate() {
		return dueDate;
	}

	public void setDueDate(LocalDate dueDate) {
		this.dueDate = dueDate;
	}

	public boolean isDone() {
		return done;
	}

	public void setDone(boolean done) {
		this.done = done;
	}

	public LocalDate getDoneDate() {
		return doneDate;
	}

	public void setDoneDate(LocalDate doneDate) {
		this.doneDate = doneDate;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}
	
}
