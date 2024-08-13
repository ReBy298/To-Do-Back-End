package com.assigment.todoapp.repository;
import org.junit.jupiter.api.Test;

import com.assigment.todoapp.domain.ToDoItem;


import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

public class ToDoRepositoryTests {

	private ToDoRepository repository;

    @Test
    public void testFetchAllToDoItems() {
        List<ToDoItem> todoItems = Arrays.asList(new ToDoItem(), new ToDoItem());
        repository = new ToDoRepository(todoItems);
        List<ToDoItem> items = repository.fetchAllToDoItems();
        assertFalse(items.isEmpty());
    }

    @Test
    public void testFindByDone() {
        ToDoItem item1 = new ToDoItem();
        item1.setDone(true);
        ToDoItem item2 = new ToDoItem();
        item2.setDone(false);
        List<ToDoItem> todoItems = Arrays.asList(item1, item2);
        repository = new ToDoRepository(todoItems);
        List<ToDoItem> items = repository.findByDone(true);
        assertTrue(items.stream().allMatch(ToDoItem::isDone));
    }

    @Test
    public void testFindByNameContainingIgnoreCase() {
        ToDoItem item1 = new ToDoItem();
        item1.setName("Test");
        ToDoItem item2 = new ToDoItem();
        item2.setName("Another");
        List<ToDoItem> todoItems = Arrays.asList(item1, item2);
        repository = new ToDoRepository(todoItems);
        String name = "test";
        List<ToDoItem> items = repository.findByNameContainingIgnoreCase(name);
        assertTrue(items.stream().allMatch(item -> item.getName().toLowerCase().contains(name.toLowerCase())));
    }

    @Test
    public void testFindByPriority() {
        ToDoItem item1 = new ToDoItem();
        item1.setPriority("High");
        ToDoItem item2 = new ToDoItem();
        item2.setPriority("Low");
        List<ToDoItem> todoItems = Arrays.asList(item1, item2);
        repository = new ToDoRepository(todoItems);
        String priority = "High";
        List<ToDoItem> items = repository.findByPriority(priority);
        assertTrue(items.stream().allMatch(item -> item.getPriority().equalsIgnoreCase(priority)));
    }
	
}
