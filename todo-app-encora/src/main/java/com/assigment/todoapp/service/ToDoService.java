package com.assigment.todoapp.service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.assigment.todoapp.domain.ToDoItem;
import com.assigment.todoapp.repository.ToDoRepository;

@Service
public class ToDoService {

	private final ToDoRepository todoRepository;

	@Autowired
	public ToDoService(ToDoRepository todoRepository) {
		this.todoRepository = todoRepository;
	}

	public List<ToDoItem> fetchAllToDoItems(String state, String name, String priority) {
		Stream<ToDoItem> stream = todoRepository.fetchAllToDoItems().stream();

		if (!"All".equals(state)) {
			boolean isDone = "Done".equals(state);
			stream = stream.filter(item -> item.isDone() == isDone);
		}

		if (!"All".equals(priority)) {
			stream = stream.filter(item -> item.getPriority().equals(priority));
		}

		if (!name.isEmpty()) {
			stream = stream.filter(item -> item.getName().contains(name));
		}

		return stream.collect(Collectors.toList());
	}

	public Map<String, Object> paginateToDoItems(List<ToDoItem> todoItems, int page, int pageSize) {
		int totalItems = todoItems.size();
		int totalPages = (int) Math.ceil((double) totalItems / pageSize);
		int startIndex = (page - 1) * pageSize;
		int endIndex = Math.min(startIndex + pageSize, totalItems);

		List<ToDoItem> paginatedItems = todoItems.subList(startIndex, endIndex);

		Map<String, Object> response = new HashMap<>();
		response.put("items", paginatedItems);
		response.put("currentPage", page);
		response.put("totalItems", totalItems);
		response.put("totalPages", totalPages);
		response.put("itemsOnPage", paginatedItems.size());

		return response;
	}

	public List<ToDoItem> sortToDoItems(List<ToDoItem> todoItems, String sortBy1, String order1, String sortBy2, String order2) {
		Comparator<ToDoItem> comparator = getComparator(sortBy1, order1, sortBy2, order2);
		todoItems.sort(comparator);
		return todoItems;
	}

	private Comparator<ToDoItem> getComparator(String sortBy1, String order1, String sortBy2, String order2) {
		Map<String, Integer> priorityValues = Map.of("Low", 1, "Medium", 2, "High", 3);

		Comparator<ToDoItem> comparator1 = getSingleComparator(sortBy1, order1, priorityValues);
		Comparator<ToDoItem> comparator2 = getSingleComparator(sortBy2, order2, priorityValues);

		return comparator1.thenComparing(comparator2);
	}

	private Comparator<ToDoItem> getSingleComparator(String sortBy, String order, Map<String, Integer> priorityValues) {
		Comparator<ToDoItem> comparator;
		if ("priority".equals(sortBy)) {
			comparator = Comparator.comparing(item -> priorityValues.getOrDefault(item.getPriority(), 0));
		} else if ("dueDate".equals(sortBy)) {
			comparator = Comparator.comparing(ToDoItem::getDueDate, Comparator.nullsLast(Comparator.naturalOrder()));
		} else {
			throw new IllegalArgumentException("Invalid sortBy: " + sortBy);
		}

		if ("desc".equals(order)) {
			comparator = comparator.reversed();
		}

		return comparator;
	}

	public ToDoItem createToDoItem(ToDoItem todoItem) {
		todoItem.setId(UUID.randomUUID());
		todoItem.setCreationDate(LocalDateTime.now());
		todoItem.setDone(false);
		todoRepository.save(todoItem);
		return todoItem;
	}

	public ToDoItem updateFlag(UUID id, ToDoItem todoItem) {
		ToDoItem existingItem = todoRepository.findById(id);
		existingItem.setDone(todoItem.isDone());
		existingItem.setDoneDate(todoItem.isDone() ? LocalDateTime.now() : null);
		todoRepository.save(existingItem);
		return existingItem;
	}

	public ToDoItem updateToDoItem(UUID id, ToDoItem updatedToDoItem) {
		ToDoItem existingItem = todoRepository.findById(id);
		existingItem.setName(updatedToDoItem.getName());
		existingItem.setDone(updatedToDoItem.isDone());
		existingItem.setPriority(updatedToDoItem.getPriority());
		existingItem.setDueDate(updatedToDoItem.getDueDate());
		todoRepository.save(existingItem);
		return existingItem;
	}

	public void deleteToDoItem(UUID id) {
		todoRepository.deleteById(id);
	}

	public List<ToDoItem> fetchAllDoneToDoItems(String priority) {
		return todoRepository.findByDone(true).stream()
				.filter(item -> "All".equals(priority) || item.getPriority().equals(priority))
				.collect(Collectors.toList());
	}

	public List<ToDoItem> fetchAllItems() {
		return todoRepository.fetchAllToDoItems();
	}

	public List<Map<String, Object>> fetchToDoItemsWithFlags() {
		List<ToDoItem> toDoItems = fetchAllItems();
		List<Map<String, Object>> toDoItemFlags = new ArrayList<>();

		for (ToDoItem item : toDoItems) {
			Map<String, Object> itemFlag = new HashMap<>();
			itemFlag.put("item", item);

			if (item.getDueDate() == null) {
				itemFlag.put("flag", 0);
			} else {
				long weeksBetween = ChronoUnit.WEEKS.between(LocalDate.now(), item.getDueDate());

				if (weeksBetween <= 1) {
					itemFlag.put("flag", 1);
				} else if (weeksBetween <= 2) {
					itemFlag.put("flag", 2);
				} else {
					itemFlag.put("flag", 3);
				}
			}

			toDoItemFlags.add(itemFlag);
		}

		return toDoItemFlags;
	}

	public double fetchAverageCompletionTime(List<ToDoItem> doneItems) {
		long totalDuration = 0;
		for (ToDoItem item : doneItems) {
			Duration duration = Duration.between(item.getCreationDate(), item.getDoneDate());
			totalDuration += duration.toMillis();
		}
		return (double) totalDuration / doneItems.size();
	}

	public Map<String, Object> fetchAverageCompletionTimes() {
		List<ToDoItem> doneItemsAll = fetchAllDoneToDoItems("All");
		List<ToDoItem> doneItemsHigh = fetchAllDoneToDoItems("High");
		List<ToDoItem> doneItemsMedium = fetchAllDoneToDoItems("Medium");
		List<ToDoItem> doneItemsLow = fetchAllDoneToDoItems("Low");

		double averageDurationAll = fetchAverageCompletionTime(doneItemsAll);
		double averageDurationHigh = fetchAverageCompletionTime(doneItemsHigh);
		double averageDurationMedium = fetchAverageCompletionTime(doneItemsMedium);
		double averageDurationLow = fetchAverageCompletionTime(doneItemsLow);

		Map<String, Object> response = new HashMap<>();
		response.put("averageTimeAll", averageDurationAll);
		response.put("averageTimeHigh", averageDurationHigh);
		response.put("averageTimeMedium", averageDurationMedium);
		response.put("averageTimeLow", averageDurationLow);

		return response;
	}
}