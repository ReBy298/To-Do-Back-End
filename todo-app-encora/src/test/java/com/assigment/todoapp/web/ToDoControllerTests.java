package com.assigment.todoapp.web;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.when;

import com.assigment.todoapp.domain.ToDoItem;
import com.assigment.todoapp.service.ToDoService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;


import java.time.LocalDateTime;
import java.util.*;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ToDoController.class) 
public class ToDoControllerTests {
	
	
	@Autowired
    private MockMvc mockMvc;

    @MockBean
    private ToDoService toDoService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
       
    }

    @Test
    void testFetchAllToDoItemsWithDefaultParams() throws Exception {
        
        ToDoItem item1 = new ToDoItem();
        item1.setId(UUID.randomUUID());
        item1.setName("Task 1");
        item1.setPriority("High");
        item1.setDone(true);
        item1.setDueDate(LocalDateTime.now());

        ToDoItem item2 = new ToDoItem();
        item2.setId(UUID.randomUUID());
        item2.setName("Task 2");
        item2.setPriority("Low");
        item2.setDone(false);
        item2.setDueDate(LocalDateTime.now().minusDays(1));

        List<ToDoItem> todoItems = Arrays.asList(item1, item2);

      
        Mockito.when(toDoService.fetchAllToDoItems("All", "", "All")).thenReturn(todoItems);

       
        ResultActions result = mockMvc.perform(get("/api/todos")
                .param("state", "All")
                .param("name", "")
                .param("priority", "All")
                .param("page", "1")
                .param("pageSize", "10")
                .param("sortBy1", "dueDate")
                .param("order1", "asc")
                .param("sortBy2", "priority")
                .param("order2", "asc"));

       
        String responseString = result.andReturn().getResponse().getContentAsString();
        System.out.println(responseString);

       
        result.andExpect(status().isOk())
              .andExpect(jsonPath("$.items").isArray())
              .andExpect(jsonPath("$.items.length()").value(2));
    }

    @Test
    void testFetchAllToDoItemsWithPagination() throws Exception {
       
        List<ToDoItem> todoItems = new ArrayList<>();
        for (int i = 1; i <= 20; i++) {
            ToDoItem item = new ToDoItem();
            item.setId(UUID.randomUUID());
            item.setName("Task " + i);
            item.setPriority("Medium");
            item.setDone(true);
            item.setDueDate(LocalDateTime.now().minusDays(i));
            todoItems.add(item);
        }

        
        Mockito.when(toDoService.fetchAllToDoItems("All", "", "All")).thenReturn(todoItems);

      
        ResultActions result = mockMvc.perform(get("/api/todos")
                .param("page", "2")
                .param("pageSize", "10")
                .param("sortBy1", "dueDate")
                .param("order1", "asc")
                .param("sortBy2", "priority")
                .param("order2", "asc"));

       
        result.andExpect(status().isOk())
              .andExpect(jsonPath("$.items.length()").value(10))
              .andExpect(jsonPath("$.currentPage").value(2))
              .andExpect(jsonPath("$.totalItems").value(20))
              .andExpect(jsonPath("$.totalPages").value(2))
              .andExpect(jsonPath("$.itemsOnPage").value(10));
    }

    @Test
    void testFetchAllToDoItemsWithSorting() throws Exception {
       
        ToDoItem item1 = new ToDoItem();
        item1.setId(UUID.randomUUID());
        item1.setName("Task 1");
        item1.setPriority("High");
        item1.setDone(true);
        item1.setDueDate(LocalDateTime.now().minusDays(1));

        ToDoItem item2 = new ToDoItem();
        item2.setId(UUID.randomUUID());
        item2.setName("Task 2");
        item2.setPriority("Low");
        item2.setDone(false);
        item2.setDueDate(LocalDateTime.now());

        List<ToDoItem> todoItems = Arrays.asList(item1, item2);

        
        Mockito.when(toDoService.fetchAllToDoItems("All", "", "All")).thenReturn(todoItems);

      
        ResultActions result = mockMvc.perform(get("/api/todos")
                .param("sortBy1", "dueDate")
                .param("order1", "asc")
                .param("sortBy2", "priority")
                .param("order2", "desc"));

       
        result.andExpect(status().isOk())
              .andExpect(jsonPath("$.items[0].name").value("Task 1"))
              .andExpect(jsonPath("$.items[1].name").value("Task 2"));
    }
    

    @Test
    void testCreateToDoItemBadRequest() throws Exception {
        ToDoItem invalidItem = new ToDoItem();
        invalidItem.setName("");

        String invalidItemJson = objectMapper.writeValueAsString(invalidItem);

        ResultActions result = mockMvc.perform(post("/api/todos")
                .contentType("application/json")
                .content(invalidItemJson));

        result.andExpect(status().isBadRequest())
              .andExpect(content().string(""));
    }
    @Test
    void testFetchAverageCompletionTime() throws Exception {
        ToDoItem item1 = new ToDoItem();
        item1.setId(UUID.randomUUID());
        item1.setName("Task 1");
        item1.setPriority("High");
        item1.setDone(true);
        item1.setCreationDate(LocalDateTime.now().minusDays(5));
        item1.setDoneDate(LocalDateTime.now());

        ToDoItem item2 = new ToDoItem();
        item2.setId(UUID.randomUUID());
        item2.setName("Task 2");
        item2.setPriority("Medium");
        item2.setDone(true);
        item2.setCreationDate(LocalDateTime.now().minusDays(3));
        item2.setDoneDate(LocalDateTime.now());

        List<ToDoItem> doneItemsAll = Arrays.asList(item1, item2);
        List<ToDoItem> doneItemsHigh = Arrays.asList(item1);
        List<ToDoItem> doneItemsMedium = Arrays.asList(item2);
        List<ToDoItem> doneItemsLow = Arrays.asList();

        when(toDoService.fetchAllDoneToDoItems("All")).thenReturn(doneItemsAll);
        when(toDoService.fetchAllDoneToDoItems("High")).thenReturn(doneItemsHigh);
        when(toDoService.fetchAllDoneToDoItems("Medium")).thenReturn(doneItemsMedium);
        when(toDoService.fetchAllDoneToDoItems("Low")).thenReturn(doneItemsLow);

        ResultActions result = mockMvc.perform(get("/api/todos/averageTime"));

        result.andExpect(status().isOk())
              .andExpect(jsonPath("$.averageTimeAll").exists())
              .andExpect(jsonPath("$.averageTimeHigh").exists())
              .andExpect(jsonPath("$.averageTimeMedium").exists())
              .andExpect(jsonPath("$.averageTimeLow").exists());
    }

    @Test
    void testDeleteToDoItem() throws Exception {
        UUID itemId = UUID.randomUUID();

        mockMvc.perform(delete("/api/todos/{id}", itemId))
                .andExpect(status().isNoContent());

        verify(toDoService, times(1)).deleteToDoItem(itemId);
    }

    @Test
    void testUpdateToDoItemBadRequest() throws Exception {
        UUID itemId = UUID.randomUUID();

        ToDoItem invalidItem = new ToDoItem();
        invalidItem.setName(""); // Invalid name to trigger bad request

        String invalidItemJson = objectMapper.writeValueAsString(invalidItem);

        ResultActions result = mockMvc.perform(put("/api/todos/{id}", itemId)
                .contentType("application/json")
                .content(invalidItemJson));

        result.andExpect(status().isBadRequest())
              .andExpect(jsonPath("$").doesNotExist());
    }
   
}