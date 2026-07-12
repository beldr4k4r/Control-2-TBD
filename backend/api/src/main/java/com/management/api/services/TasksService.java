package com.management.api.services;

import java.util.List;
import java.util.Optional;

import javax.management.RuntimeErrorException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.config.Task;
import org.springframework.stereotype.Service;

import com.management.api.models.Tasks;
import com.management.api.repositories.TasksRepository;

import jakarta.transaction.Transactional;

@Service
public class TasksService {

    @Autowired
    private TasksRepository tasksRepository;

    public List<Tasks> getTasksByUserId(Long userId){
        return tasksRepository.findByUserIdAuth(userId);
    }

    public Optional<Tasks> getTaskById(Long userId){
        return tasksRepository.findById(userId);
    }

    @Transactional
    public Tasks saveTask(Tasks tasksSave){
        return tasksRepository.save(tasksSave);
    }

    @Transactional
    public void deleteTask(Long taskId){
        if(tasksRepository.existsById(taskId)){
            tasksRepository.deleteById(taskId);
        }else{
            throw new RuntimeException("Error: La tarea elegida no existe");
        }
    }

    @Transactional
    public Tasks completedTask(Long taskId){
        if(tasksRepository.existsById(taskId)){
            Optional<Tasks> task = tasksRepository.findById(taskId);
            Tasks completeTask = task.get();
            completeTask.setComplete_task(true);
            return tasksRepository.save(completeTask);
        }else{
            throw new RuntimeException("Error: La tarea no se encontro");
        }
    }

    public Tasks getNearestPendingTask(Long userId) {
        return tasksRepository.findNearestPendingTask(userId);
    }

    public Double getAverageDistanceOfCompleted(Long userId) {
        return tasksRepository.getAverageDistanceOfCompletedTasks(userId);
    }

    public List<Tasks> getTasksBySector(Long sectorId) {
        return tasksRepository.findBySector(sectorId);
    }
}