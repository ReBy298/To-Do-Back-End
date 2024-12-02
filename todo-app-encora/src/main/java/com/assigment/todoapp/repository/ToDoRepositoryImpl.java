package com.assigment.todoapp.repository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.assigment.todoapp.domain.ToDoItem;

@Repository
public class ToDoRepositoryImpl implements ToDoRepository {

    private final List<ToDoItem> todoItems;

    public ToDoRepositoryImpl(List<ToDoItem> todoItems) {
        this.todoItems = todoItems;
    }

    @Override
    public List<ToDoItem> fetchAllToDoItems() {
        return todoItems;
    }

    @Override
    public List<ToDoItem> findByDone(boolean done) {
        return todoItems.stream()
                .filter(item -> item.isDone() == done)
                .collect(Collectors.toList());
    }

    @Override
    public List<ToDoItem> findByNameContainingIgnoreCase(String name) {
        return todoItems.stream()
                .filter(item -> item.getName().toLowerCase().contains(name.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public List<ToDoItem> findByPriority(String priority) {
        return todoItems.stream()
                .filter(item -> item.getPriority().equalsIgnoreCase(priority))
                .collect(Collectors.toList());
    }

    @Override
    public ToDoItem findById(UUID id) {
        return todoItems.stream()
                .filter(item -> item.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("ToDoItem not found"));
    }

    @Override
    public void save(ToDoItem todoItem) {
        todoItems.add(todoItem);
    }

    @Override
    public void deleteById(UUID id) {
        todoItems.removeIf(item -> item.getId().equals(id));
    }
}