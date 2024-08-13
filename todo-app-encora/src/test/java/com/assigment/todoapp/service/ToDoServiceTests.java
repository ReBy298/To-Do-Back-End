package com.assigment.todoapp.service;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertThrows;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import com.assigment.todoapp.domain.ToDoItem;
import com.assigment.todoapp.repository.ToDoRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest
public class ToDoServiceTests {

	@Mock
    private ToDoRepository todoRepository;

	@InjectMocks
    private ToDoService toDoService;

    private List<ToDoItem> todoItems;

    @BeforeEach
    void setUp() {
    	MockitoAnnotations.openMocks(this);
        todoItems = new ArrayList<>();
        toDoService.setTodoItems(todoItems); 
        
    }

    @Test
    void testFetchAllToDoItemsByStateDone() {
       
        ToDoItem item1 = new ToDoItem();
        item1.setName("Test 1");
        item1.setPriority("High");
        item1.setDone(true); 

        ToDoItem item2 = new ToDoItem();
        item2.setName("Test 2");
        item2.setPriority("Low");
        item2.setDone(false); 

        List<ToDoItem> items = Arrays.asList(item1, item2);
        when(todoRepository.fetchAllToDoItems()).thenReturn(items);

        List<ToDoItem> result = toDoService.fetchAllToDoItems("Done", "", "All");

        assertEquals(1, result.size());
        assertTrue(result.contains(item1));
        assertFalse(result.contains(item2));
    }

    @Test
    void testFetchAllToDoItemsByStateNotDone() {
       
        ToDoItem item1 = new ToDoItem();
        item1.setName("Test 1");
        item1.setPriority("High");
        item1.setDone(false); 

        ToDoItem item2 = new ToDoItem();
        item2.setName("Test 2");
        item2.setPriority("Low");
        item2.setDone(true); 

        List<ToDoItem> items = Arrays.asList(item1, item2);
        when(todoRepository.fetchAllToDoItems()).thenReturn(items);

        List<ToDoItem> result = toDoService.fetchAllToDoItems("Not Done", "", "All");

        assertEquals(1, result.size());
        assertTrue(result.contains(item1));
        assertFalse(result.contains(item2));
    }

    @Test
    void testFetchAllToDoItemsByPriority() {
    
        ToDoItem item1 = new ToDoItem();
        item1.setName("Test 1");
        item1.setPriority("High"); 
        item1.setDone(true);

        ToDoItem item2 = new ToDoItem();
        item2.setName("Test 2");
        item2.setPriority("Low"); 
        item2.setDone(true);

        List<ToDoItem> items = Arrays.asList(item1, item2);
        when(todoRepository.fetchAllToDoItems()).thenReturn(items);

        List<ToDoItem> result = toDoService.fetchAllToDoItems("All", "", "High");

        assertEquals(1, result.size());
        assertTrue(result.contains(item1));
        assertFalse(result.contains(item2));
    }

    @Test
    void testFetchAllToDoItemsByName() {
     
        ToDoItem item1 = new ToDoItem();
        item1.setName("Test 1"); 
        item1.setPriority("High");
        item1.setDone(true);

        ToDoItem item2 = new ToDoItem();
        item2.setName("Sample 2"); 
        item2.setPriority("Low");
        item2.setDone(true);

        List<ToDoItem> items = Arrays.asList(item1, item2);
        when(todoRepository.fetchAllToDoItems()).thenReturn(items);

        List<ToDoItem> result = toDoService.fetchAllToDoItems("All", "Test", "All");

        assertEquals(1, result.size());
        assertTrue(result.contains(item1));
        assertFalse(result.contains(item2));
    }
	

	@Test
    public void testFetchAllToDoItems() {
        ToDoItem item1 = new ToDoItem();
        item1.setId(UUID.randomUUID());
        item1.setName("Task 1");
        item1.setDueDate(LocalDateTime.now());
        item1.setDone(false);
        item1.setDoneDate(LocalDateTime.now());
        item1.setPriority("High");
        item1.setCreationDate(LocalDateTime.now());

        ToDoItem item2 = new ToDoItem();
        item2.setId(UUID.randomUUID());
        item2.setName("Task 2");
        item2.setDueDate(LocalDateTime.now());
        item2.setDone(true);
        item2.setDoneDate(LocalDateTime.now());
        item2.setPriority("Low");
        item2.setCreationDate(LocalDateTime.now());

        List<ToDoItem> expectedItems = Arrays.asList(item1, item2);

        when(todoRepository.fetchAllToDoItems()).thenReturn(expectedItems);

        List<ToDoItem> actualItems = toDoService.fetchAllToDoItems("All", "", "All");

        assertEquals(expectedItems, actualItems);
    }
	
	
	@Test
    void testUpdateToDoItemWhenItemExists() {
        UUID id = UUID.randomUUID();
        ToDoItem existingItem = new ToDoItem();
        existingItem.setId(id);
        existingItem.setName("Old Name");
        existingItem.setDone(false);
        existingItem.setPriority("Medium");
        existingItem.setDueDate(LocalDateTime.of(2024, 1, 1, 10, 0));

        todoItems.add(existingItem);

        ToDoItem updatedItem = new ToDoItem();
        updatedItem.setName("New Name");
        updatedItem.setDone(true);
        updatedItem.setPriority("High");
        updatedItem.setDueDate(LocalDateTime.of(2024, 12, 31, 10, 0));

        ToDoItem result = toDoService.updateToDoItem(id, updatedItem);

        assertEquals("New Name", result.getName());
        assertTrue(result.isDone());
        assertEquals("High", result.getPriority());
        assertEquals(LocalDateTime.of(2024, 12, 31, 10, 0), result.getDueDate());
    }

    @Test
    void testUpdateToDoItemWhenItemDoesNotExist() {
        UUID id = UUID.randomUUID();
        ToDoItem updatedItem = new ToDoItem();
        updatedItem.setName("New Name");
        updatedItem.setDone(true);
        updatedItem.setPriority("High");
        updatedItem.setDueDate(LocalDateTime.of(2024, 12, 31, 10, 0));

        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
            toDoService.updateToDoItem(id, updatedItem);
        });

        assertEquals("ToDoItem no encontrado", thrown.getMessage());
    }
    
    @Test
    void testDeleteToDoItemWhenItemExists() {
        UUID id = UUID.randomUUID();
        ToDoItem item = new ToDoItem();
        item.setId(id);
        item.setName("Sample Item");

        todoItems.add(item);

        assertTrue(todoItems.stream().anyMatch(i -> i.getId().equals(id)));

        toDoService.deleteToDoItem(id);

        assertFalse(todoItems.stream().anyMatch(i -> i.getId().equals(id)));
    }

    @Test
    void testDeleteToDoItemWhenItemDoesNotExist() {
        UUID id = UUID.randomUUID();

        assertFalse(todoItems.stream().anyMatch(i -> i.getId().equals(id)));

        toDoService.deleteToDoItem(id);

        assertTrue(todoItems.isEmpty());
    }
	 @Test
	    void testUpdateFlagWhenItemExistsAndIsNotDone() {
	        UUID id = UUID.randomUUID();
	        ToDoItem existingItem = new ToDoItem();
	        existingItem.setId(id);
	        existingItem.setDone(false);
	        existingItem.setDoneDate(null);
	        
	        todoItems.add(existingItem);

	        ToDoItem updateItem = new ToDoItem();
	        updateItem.setDone(true);

	        ToDoItem updatedItem = toDoService.updateFlag(id, updateItem);

	        assertTrue(updatedItem.isDone());
	        assertNotNull(updatedItem.getDoneDate());
	        assertEquals(id, updatedItem.getId());
	    }

	 @Test
	    void testFetchAllDoneToDoItemsWithSpecificPriority() {
	        UUID id1 = UUID.randomUUID();
	        ToDoItem item1 = new ToDoItem();
	        item1.setId(id1);
	        item1.setName("Task 1");
	        item1.setPriority("High");
	        item1.setDone(true);
	        todoItems.add(item1);

	        UUID id2 = UUID.randomUUID();
	        ToDoItem item2 = new ToDoItem();
	        item2.setId(id2);
	        item2.setName("Task 2");
	        item2.setPriority("Low");
	        item2.setDone(true);
	        todoItems.add(item2);

	        UUID id3 = UUID.randomUUID();
	        ToDoItem item3 = new ToDoItem();
	        item3.setId(id3);
	        item3.setName("Task 3");
	        item3.setPriority("High");
	        item3.setDone(false);
	        todoItems.add(item3);

	        List<ToDoItem> result = toDoService.fetchAllDoneToDoItems("High");

	        assertEquals(1, result.size());
	        assertEquals("Task 1", result.get(0).getName());
	    }

	    @Test
	    void testFetchAllDoneToDoItemsWithAllPriority() {
	        UUID id1 = UUID.randomUUID();
	        ToDoItem item1 = new ToDoItem();
	        item1.setId(id1);
	        item1.setName("Task 1");
	        item1.setPriority("High");
	        item1.setDone(true);
	        todoItems.add(item1);

	        UUID id2 = UUID.randomUUID();
	        ToDoItem item2 = new ToDoItem();
	        item2.setId(id2);
	        item2.setName("Task 2");
	        item2.setPriority("Low");
	        item2.setDone(true);
	        todoItems.add(item2);

	        UUID id3 = UUID.randomUUID();
	        ToDoItem item3 = new ToDoItem();
	        item3.setId(id3);
	        item3.setName("Task 3");
	        item3.setPriority("High");
	        item3.setDone(false);
	        todoItems.add(item3);

	        List<ToDoItem> result = toDoService.fetchAllDoneToDoItems("All");

	        assertEquals(2, result.size());
	        assertEquals("Task 1", result.get(0).getName());
	        assertEquals("Task 2", result.get(1).getName());
	    }

	    @Test
	    void testFetchAllDoneToDoItemsWhenNoDoneItems() {
	        UUID id1 = UUID.randomUUID();
	        ToDoItem item1 = new ToDoItem();
	        item1.setId(id1);
	        item1.setName("Task 1");
	        item1.setPriority("High");
	        item1.setDone(false);
	        todoItems.add(item1);

	        UUID id2 = UUID.randomUUID();
	        ToDoItem item2 = new ToDoItem();
	        item2.setId(id2);
	        item2.setName("Task 2");
	        item2.setPriority("Low");
	        item2.setDone(false);
	        todoItems.add(item2);

	        List<ToDoItem> result = toDoService.fetchAllDoneToDoItems("All");

	        assertEquals(0, result.size());
	    }
	    @Test
	    void testUpdateFlagWhenItemDoesNotExist() {
	        UUID id = UUID.randomUUID(); // UUID que no está en la lista
	        ToDoItem updateItem = new ToDoItem();
	        updateItem.setDone(true);

	        RuntimeException thrown = assertThrows(RuntimeException.class, () -> {
	            toDoService.updateFlag(id, updateItem);
	        });

	        assertEquals("ToDoItem no encontrado", thrown.getMessage());
	    }

	    
	    @Test
	    void testUpdateFlagWhenItemExistsAndIsDone() {
	        UUID id = UUID.randomUUID();
	        ToDoItem existingItem = new ToDoItem();
	        existingItem.setId(id);
	        existingItem.setDone(true);
	        existingItem.setDoneDate(LocalDateTime.now());
	        todoItems.add(existingItem);

	        ToDoItem updateItem = new ToDoItem();
	        updateItem.setDone(false);

	        ToDoItem result = toDoService.updateFlag(id, updateItem);

	        assertEquals(false, result.isDone());
	        assertEquals(null, result.getDoneDate());
	    }
    @Test
    public void testCreateToDoItem() {
        ToDoItem item = new ToDoItem();
        item.setName("Test");
        item.setPriority("High");

        ToDoItem createdItem = toDoService.createToDoItem(item);

        assertEquals("Test", createdItem.getName());
        assertEquals("High", createdItem.getPriority());
        assertEquals(false, createdItem.isDone());
    }
    
    

   
    @Test
    public void testDeleteToDoItem() {
        // Create a ToDoItem for testing
        ToDoItem item = new ToDoItem();
        UUID id = UUID.randomUUID();
        item.setId(id);
        item.setName("Test");
        item.setDone(false);
        item.setPriority("High");
        item.setCreationDate(LocalDateTime.now());

        // Set up the mock behavior
        when(todoRepository.fetchAllToDoItems()).thenReturn(Arrays.asList(item));

        // Delete the ToDoItem
        toDoService.deleteToDoItem(id);

        // Verify that the item was deleted
        List<ToDoItem> items = toDoService.fetchAllItems();
        assertEquals(0, items.size());
    }

 



    
}