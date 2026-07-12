package com.management.api.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.management.api.models.Tasks;
import com.management.api.services.TasksService;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.config.Task;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api/tasks")
@CrossOrigin(origins = "*")
public class TasksController {

    @Autowired
    private TasksService tasksService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Tasks>> getTasksByUserId(@PathVariable Long userId) {
        List<Tasks> tasks = tasksService.getTasksByUserId(userId);
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tasks> getTaskById(@PathVariable Long id) {
        Optional<Tasks> task = tasksService.getTaskById(id);
        return task.map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                   .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/{id}/sector")
    public ResponseEntity<List<Tasks>> getSector(@PathVariable Long id) {
        List<Tasks> tasks = tasksService.getTasksBySector(id);
        return ResponseEntity.ok(tasks);
    }   

    @PostMapping
    public ResponseEntity<Tasks> createTask(@RequestBody Tasks task) {
        Tasks newTask = tasksService.saveTask(task);
        return new ResponseEntity<>(newTask, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Tasks> updateTask(@PathVariable Long id, @RequestBody Tasks taskDetails) {
        Optional<Tasks> existingTask = tasksService.getTaskById(id);
        if (existingTask.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        taskDetails.setId_task(id);
        Tasks updatedTask = tasksService.saveTask(taskDetails);
        return new ResponseEntity<>(updatedTask, HttpStatus.OK);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        try {
            tasksService.deleteTask(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); 
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/{id}/complete")
    public ResponseEntity<Tasks> markTaskAsCompleted(@PathVariable Long id) {
        try {
            Tasks completedTask = tasksService.completedTask(id);
            return new ResponseEntity<>(completedTask, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/user/{userId}/nearest")
    public ResponseEntity<Tasks> getNearestPendingTask(@PathVariable Long userId) {
        Tasks nearestTask = tasksService.getNearestPendingTask(userId);
        if (nearestTask != null) {
            return new ResponseEntity<>(nearestTask, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/user/{userId}/average-distance")
    public ResponseEntity<Double> getAverageDistanceOfCompleted(@PathVariable Long userId) {
        Double avgDistance = tasksService.getAverageDistanceOfCompleted(userId);
        return new ResponseEntity<>(avgDistance, HttpStatus.OK);
    }
}