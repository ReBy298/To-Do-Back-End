package com.assigment.todoapp.repository;

import java.util.List;
import java.util.UUID;

import com.assigment.todoapp.domain.ToDoItem;

public interface ToDoRepository {
    List<ToDoItem> fetchAllToDoItems();
    List<ToDoItem> findByDone(boolean done);
    List<ToDoItem> findByNameContainingIgnoreCase(String name);
    List<ToDoItem> findByPriority(String priority);
    ToDoItem findById(UUID id);
    void save(ToDoItem todoItem);
    void deleteById(UUID id);
}