package com.assigment.todoapp.repository;

import com.assigment.todoapp.domain.ToDoItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class ToDoRepositoryImplTests {

    private ToDoRepositoryImpl repository;
    private List<ToDoItem> todoItems;

    @BeforeEach
    public void setUp() {
        todoItems = Arrays.asList(
                createToDoItem("Task 1", "High", true),
                createToDoItem("Task 2", "Low", false),
                createToDoItem("Task 3", "Medium", true)
        );
        repository = new ToDoRepositoryImpl(todoItems);
    }

    @Test
    public void testFetchAllToDoItems() {
        List<ToDoItem> items = repository.fetchAllToDoItems();
        assertEquals(3, items.size());
    }

    @Test
    public void testFindByDone() {
        List<ToDoItem> doneItems = repository.findByDone(true);
        assertEquals(2, doneItems.size());
        assertTrue(doneItems.stream().allMatch(ToDoItem::isDone));
    }

    @Test
    public void testFindByNameContainingIgnoreCase() {
        List<ToDoItem> items = repository.findByNameContainingIgnoreCase("task");
        assertEquals(3, items.size());
    }

    @Test
    public void testFindByPriority() {
        List<ToDoItem> highPriorityItems = repository.findByPriority("High");
        assertEquals(1, highPriorityItems.size());
        assertTrue(highPriorityItems.stream().allMatch(item -> "High".equals(item.getPriority())));
    }

    @Test
    public void testFindById() {
        UUID id = todoItems.get(0).getId();
        ToDoItem item = repository.findById(id);
        assertNotNull(item);
        assertEquals(id, item.getId());
    }


    private ToDoItem createToDoItem(String name, String priority, boolean done) {
        ToDoItem item = new ToDoItem();
        item.setName(name);
        item.setPriority(priority);
        item.setDone(done);
        item.setId(UUID.randomUUID());
        return item;
    }
}