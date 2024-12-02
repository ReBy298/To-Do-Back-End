package com.assigment.todoapp.controller;

import com.assigment.todoapp.domain.ToDoItem;
import com.assigment.todoapp.service.ToDoService;
import com.assigment.todoapp.web.ToDoController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.*;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ToDoControllerTests {

    private MockMvc mockMvc;

    @Mock
    private ToDoService toDoService;

    @InjectMocks
    private ToDoController toDoController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(toDoController).build();
    }

    @Test
    void testFetchAllToDoItems() throws Exception {
        List<ToDoItem> items = Arrays.asList(new ToDoItem(), new ToDoItem());
        when(toDoService.fetchAllToDoItems(anyString(), anyString(), anyString())).thenReturn(items);
        when(toDoService.sortToDoItems(anyList(), anyString(), anyString(), anyString(), anyString())).thenReturn(items);
        when(toDoService.paginateToDoItems(anyList(), anyInt(), anyInt())).thenReturn(new HashMap<>());

        mockMvc.perform(get("/api/todos")
                        .param("state", "All")
                        .param("name", "")
                        .param("priority", "All")
                        .param("page", "1")
                        .param("pageSize", "10")
                        .param("sortBy1", "dueDate")
                        .param("order1", "asc")
                        .param("sortBy2", "priority")
                        .param("order2", "asc"))
                .andExpect(status().isOk());
    }

    @Test
    void testCreateToDoItem() throws Exception {
        ToDoItem item = new ToDoItem();
        item.setName("Test Item");
        when(toDoService.createToDoItem(any(ToDoItem.class))).thenReturn(item);

        mockMvc.perform(post("/api/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Test Item\"}"))
                .andExpect(status().isCreated());
    }

    @Test
    void testUpdateFlag() throws Exception {
        ToDoItem item = new ToDoItem();
        item.setDone(true);
        when(toDoService.updateFlag(any(UUID.class), any(ToDoItem.class))).thenReturn(item);

        mockMvc.perform(post("/api/todos/{id}/done", UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"done\":true}"))
                .andExpect(status().isOk());
    }

    @Test
    void testFetchToDoItemsWithFlags() throws Exception {
        when(toDoService.fetchToDoItemsWithFlags()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/api/todos/colorFlags"))
                .andExpect(status().isOk());
    }

    @Test
    void testFetchAverageCompletionTime() throws Exception {
        when(toDoService.fetchAverageCompletionTimes()).thenReturn(new HashMap<>());

        mockMvc.perform(get("/api/todos/averageTime"))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdateToDoItem() throws Exception {
        ToDoItem item = new ToDoItem();
        item.setName("Updated Item");
        when(toDoService.updateToDoItem(any(UUID.class), any(ToDoItem.class))).thenReturn(item);

        mockMvc.perform(put("/api/todos/{id}", UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Updated Item\"}"))
                .andExpect(status().isOk());
    }

    @Test
    void testDeleteToDoItem() throws Exception {
        doNothing().when(toDoService).deleteToDoItem(any(UUID.class));

        mockMvc.perform(delete("/api/todos/{id}", UUID.randomUUID()))
                .andExpect(status().isNoContent());
    }
}