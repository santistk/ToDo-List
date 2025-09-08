package com.jaestradag.todolist.repositories;

import com.jaestradag.todolist.models.Task;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;


@ApplicationScoped
public class TaskRepository {
    
    private final Map<Long, Task> data = new ConcurrentHashMap<>();
    private final AtomicLong sequence = new AtomicLong(0);
    

    public List<Task> findAll() {
        return new ArrayList<>(data.values());
    }
    
 
    public List<Task> findPending() {
        return data.values().stream()
                .filter(task -> !task.isCompleted())
                .collect(Collectors.toList());
    }
    

    public List<Task> findCompleted() {
        return data.values().stream()
                .filter(Task::isCompleted)
                .collect(Collectors.toList());
    }
    

    public Optional<Task> findById(Long id) {
        return Optional.ofNullable(data.get(id));
    }
    

    public Task save(Task task) {
        if (task.getId() == null) {
            task.setId(sequence.incrementAndGet());
        }
        data.put(task.getId(), task);
        return task;
    }
    

    public boolean deleteById(Long id) {
        return data.remove(id) != null;
    }
    

    public int clearCompleted() {
        List<Long> completedIds = data.values().stream()
                .filter(Task::isCompleted)
                .map(Task::getId)
                .collect(Collectors.toList());
        
        completedIds.forEach(data::remove);
        return completedIds.size();
    }
    

    public long count() {
        return data.size();
    }
    

    public long countPending() {
        return data.values().stream()
                .filter(task -> !task.isCompleted())
                .count();
    }
    

    public long countCompleted() {
        return data.values().stream()
                .filter(Task::isCompleted)
                .count();
    }
}

