package com.assigment.todoapp.domain;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.UUID;

public class ToDoItemTests {
	
	@Test
    public void testToDoItem() {
 
        ToDoItem item = new ToDoItem();

 
        String name = "Test";
        LocalDateTime dueDate = LocalDateTime.now();
        boolean done = false;
        LocalDateTime doneDate = LocalDateTime.now();
        String priority = "High";
        LocalDateTime creationDate = LocalDateTime.now();

        item.setName(name);
        item.setDueDate(dueDate);
        item.setDone(done);
        item.setDoneDate(doneDate);
        item.setPriority(priority);
        item.setCreationDate(creationDate);

   
        assertEquals(name, item.getName());
        assertEquals(dueDate, item.getDueDate());
        assertEquals(done, item.isDone());
        assertEquals(doneDate, item.getDoneDate());
        assertEquals(priority, item.getPriority());
        assertEquals(creationDate, item.getCreationDate());
    }
	
	@Test
    public void testGetId() {
        ToDoItem item = new ToDoItem();
        UUID id = UUID.randomUUID();
        item.setId(id);
        assertEquals(id, item.getId());
    }

    @Test
    public void testGetName() {
        ToDoItem item = new ToDoItem();
        String name = "Test Name";
        item.setName(name);
        assertEquals(name, item.getName());
    }

    @Test
    public void testGetDueDate() {
        ToDoItem item = new ToDoItem();
        LocalDateTime dueDate = LocalDateTime.now();
        item.setDueDate(dueDate);
        assertEquals(dueDate, item.getDueDate());
    }

    @Test
    public void testIsDone() {
        ToDoItem item = new ToDoItem();
        item.setDone(true);
        assertTrue(item.isDone());
    }

    @Test
    public void testGetDoneDate() {
        ToDoItem item = new ToDoItem();
        LocalDateTime doneDate = LocalDateTime.now();
        item.setDoneDate(doneDate);
        assertEquals(doneDate, item.getDoneDate());
    }

    @Test
    public void testGetPriority() {
        ToDoItem item = new ToDoItem();
        String priority = "High";
        item.setPriority(priority);
        assertEquals(priority, item.getPriority());
    }

    @Test
    public void testGetCreationDate() {
        ToDoItem item = new ToDoItem();
        LocalDateTime creationDate = LocalDateTime.now();
        item.setCreationDate(creationDate);
        assertEquals(creationDate, item.getCreationDate());
    }

    @Test
    public void testSetId() {
        ToDoItem item = new ToDoItem();
        UUID id = UUID.randomUUID();
        item.setId(id);
        assertEquals(id, item.getId());
    }

    @Test
    public void testSetName() {
        ToDoItem item = new ToDoItem();
        String name = "Test Name";
        item.setName(name);
        assertEquals(name, item.getName());
    }

    @Test
    public void testSetDueDate() {
        ToDoItem item = new ToDoItem();
        LocalDateTime dueDate = LocalDateTime.now();
        item.setDueDate(dueDate);
        assertEquals(dueDate, item.getDueDate());
    }

    @Test
    public void testSetDone() {
        ToDoItem item = new ToDoItem();
        item.setDone(true);
        assertTrue(item.isDone());
    }

    @Test
    public void testSetDoneDate() {
        ToDoItem item = new ToDoItem();
        LocalDateTime doneDate = LocalDateTime.now();
        item.setDoneDate(doneDate);
        assertEquals(doneDate, item.getDoneDate());
    }

    @Test
    public void testSetPriority() {
        ToDoItem item = new ToDoItem();

        assertDoesNotThrow(() -> item.setPriority("High"));
        assertEquals("High", item.getPriority());

        assertThrows(IllegalArgumentException.class, () -> item.setPriority("Very High"));
    }

    @Test
    public void testSetCreationDate() {
        ToDoItem item = new ToDoItem();
        LocalDateTime creationDate = LocalDateTime.now();
        item.setCreationDate(creationDate);
        assertEquals(creationDate, item.getCreationDate());
    }
	
	
	
	
}
