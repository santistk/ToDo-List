package com.jaestradag.todolist.controllers;

import com.jaestradag.todolist.models.Task;
import com.jaestradag.todolist.services.TaskService;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serializable;
import java.util.List;


@Named
@SessionScoped
public class TaskController implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    @Inject
    private TaskService taskService;
    
    private String newTaskDescription = "";
    private String filterType = "all"; 
    

    public String getNewTaskDescription() {
        return newTaskDescription;
    }
    
    public void setNewTaskDescription(String newTaskDescription) {
        this.newTaskDescription = newTaskDescription;
    }
    
    public String getFilterType() {
        return filterType;
    }
    
    public void setFilterType(String filterType) {
        this.filterType = filterType;
    }
    
   
    public void addTask() {
        try {
            if (newTaskDescription != null && !newTaskDescription.trim().isEmpty()) {
                taskService.addTask(newTaskDescription);
                addMessage("Éxito", "Tarea agregada correctamente");
                newTaskDescription = ""; 
            } else {
                addMessage("Error", "La descripción de la tarea no puede estar vacía");
            }
        } catch (Exception e) {
            addMessage("Error", "Error al agregar la tarea: " + e.getMessage());
        }
    }
    

    public void toggleTaskCompletion(Long taskId) {
        try {
            taskService.toggleTaskCompletion(taskId);
            addMessage("Éxito", "Estado de la tarea actualizado");
        } catch (Exception e) {
            addMessage("Error", "Error al actualizar la tarea: " + e.getMessage());
        }
    }
    

    public void deleteTask(Long taskId) {
        try {
            if (taskService.deleteTask(taskId)) {
                addMessage("Éxito", "Tarea eliminada correctamente");
            } else {
                addMessage("Error", "No se pudo eliminar la tarea");
            }
        } catch (Exception e) {
            addMessage("Error", "Error al eliminar la tarea: " + e.getMessage());
        }
    }
    

    public void clearCompletedTasks() {
        try {
            int deletedCount = taskService.clearCompletedTasks();
            if (deletedCount > 0) {
                addMessage("Éxito", deletedCount + " tareas completadas eliminadas");
            } else {
                addMessage("Info", "No hay tareas completadas para eliminar");
            }
        } catch (Exception e) {
            addMessage("Error", "Error al eliminar las tareas completadas: " + e.getMessage());
        }
    }
    

    public List<Task> getAllTasks() {
        switch (filterType) {
            case "pending":
                return taskService.getPendingTasks();
            case "completed":
                return taskService.getCompletedTasks();
            default:
                return taskService.getAllTasks();
        }
    }
    
  
    public void filterPending() {
        filterType = "pending";
    }
    

    public void filterCompleted() {
        filterType = "completed";
    }
    
 
    public void filterAll() {
        filterType = "all";
    }
    

    public long getTotalTaskCount() {
        return taskService.getTotalTaskCount();
    }
    
    public long getPendingTaskCount() {
        return taskService.getPendingTaskCount();
    }
    
    public long getCompletedTaskCount() {
        return taskService.getCompletedTaskCount();
    }
    

    public String getTaskStatusStyle(Task task) {
        return task.isCompleted() ? "completed" : "pending";
    }
    

    private void addMessage(String severity, String message) {
        FacesContext.getCurrentInstance().addMessage(null, 
            new FacesMessage(FacesMessage.SEVERITY_INFO, severity, message));
    }
}

