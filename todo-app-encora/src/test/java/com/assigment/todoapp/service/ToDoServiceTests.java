package com.assigment.todoapp.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import com.assigment.todoapp.domain.ToDoItem;
import com.assigment.todoapp.repository.ToDoRepository;

import java.time.LocalDateTime;
import java.util.*;

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
    }



    @Test
    void testFetchAllDoneToDoItemsWhenNoDoneItems() {
        ToDoItem item1 = new ToDoItem();
        item1.setName("Task 1");
        item1.setPriority("High");
        item1.setDone(false);

        ToDoItem item2 = new ToDoItem();
        item2.setName("Task 2");
        item2.setPriority("Low");
        item2.setDone(false);

        List<ToDoItem> items = Arrays.asList(item1, item2);
        when(todoRepository.findByDone(true)).thenReturn(Collections.emptyList());

        List<ToDoItem> result = toDoService.fetchAllDoneToDoItems("All");

        assertEquals(0, result.size());
    }

    @Test
    void testPaginateToDoItems_FirstPage() {
        List<ToDoItem> items = createTestToDoItems(25);
        int page = 1;
        int pageSize = 10;

        Map<String, Object> response = toDoService.paginateToDoItems(items, page, pageSize);

        assertEquals(10, ((List<?>) response.get("items")).size());
        assertEquals(1, response.get("currentPage"));
        assertEquals(25, response.get("totalItems"));
        assertEquals(3, response.get("totalPages"));
        assertEquals(10, response.get("itemsOnPage"));
    }

    @Test
    void testPaginateToDoItems_SecondPage() {
        List<ToDoItem> items = createTestToDoItems(25);
        int page = 2;
        int pageSize = 10;

        Map<String, Object> response = toDoService.paginateToDoItems(items, page, pageSize);

        assertEquals(10, ((List<?>) response.get("items")).size());
        assertEquals(2, response.get("currentPage"));
        assertEquals(25, response.get("totalItems"));
        assertEquals(3, response.get("totalPages"));
        assertEquals(10, response.get("itemsOnPage"));
    }

    @Test
    void testPaginateToDoItems_ThirdPage() {
        List<ToDoItem> items = createTestToDoItems(25);
        int page = 3;
        int pageSize = 10;

        Map<String, Object> response = toDoService.paginateToDoItems(items, page, pageSize);

        assertEquals(5, ((List<?>) response.get("items")).size());
        assertEquals(3, response.get("currentPage"));
        assertEquals(25, response.get("totalItems"));
        assertEquals(3, response.get("totalPages"));
        assertEquals(5, response.get("itemsOnPage"));
    }

    private List<ToDoItem> createTestToDoItems(int count) {
        List<ToDoItem> items = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            ToDoItem item = new ToDoItem();
            item.setName("Test " + i);
            item.setPriority("High");
            item.setDone(true);
            items.add(item);
        }
        return items;
    }
}