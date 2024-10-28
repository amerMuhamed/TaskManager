package com.store.store.service;

import com.store.store.entity.Task;

import java.util.List;

public interface TaskService {
    public void saveTask(Task task);

   public List<Task> getAllTasks();

   public void deleteTask(Task task);

   public Task findTaskById(int theId);

   Task findTaskByUserId(String userId);
}
