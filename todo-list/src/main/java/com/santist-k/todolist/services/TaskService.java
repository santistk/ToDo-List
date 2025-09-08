package com.jaestradag.todolist.services;

import com.jaestradag.todolist.models.Task;
import com.jaestradag.todolist.repositories.TaskRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;
import java.util.Optional;


@ApplicationScoped
public class TaskService {
    
    @Inject
    private TaskRepository taskRepository;
    

    public Task addTask(String description) {
        if (description == null || description.trim().isEmpty()) {
            throw new IllegalArgumentException("La descripción de la tarea no puede estar vacía");
        }
        
        Task task = new Task(description.trim());
        return taskRepository.save(task);
    }
    

    public Task toggleTaskCompletion(Long taskId) {
        Optional<Task> taskOpt = taskRepository.findById(taskId);
        if (taskOpt.isPresent()) {
            Task task = taskOpt.get();
            task.setCompleted(!task.isCompleted());
            return taskRepository.save(task);
        }
        throw new IllegalArgumentException("Tarea no encontrada con ID: " + taskId);
    }
    

    public boolean deleteTask(Long taskId) {
        return taskRepository.deleteById(taskId);
    }
    

    public int clearCompletedTasks() {
        return taskRepository.clearCompleted();
    }
    

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }
    

    public List<Task> getPendingTasks() {
        return taskRepository.findPending();
    }
    

    public List<Task> getCompletedTasks() {
        return taskRepository.findCompleted();
    }
    

    public long getTotalTaskCount() {
        return taskRepository.count();
    }
    

    public long getPendingTaskCount() {
        return taskRepository.countPending();
    }
    

    public long getCompletedTaskCount() {
        return taskRepository.countCompleted();
    }
    

    public Optional<Task> getTaskById(Long id) {
        return taskRepository.findById(id);
    }
}

