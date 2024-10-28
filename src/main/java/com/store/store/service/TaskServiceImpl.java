package com.store.store.service;

import com.store.store.dao.TaskDAORepository;
import com.store.store.entity.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskServiceImpl implements TaskService{
    private TaskDAORepository taskDAORepository;
@Autowired
    public TaskServiceImpl(TaskDAORepository taskDAORepository) {
        this.taskDAORepository = taskDAORepository;
    }

    @Override
    public void saveTask(Task task) {
        taskDAORepository.save(task);
    }

    @Override
    public List<Task> getAllTasks() {
        return taskDAORepository.findAll();
    }

    @Override
    public void deleteTask(Task task) {
        taskDAORepository.delete(task);
    }

    @Override
    public Task findTaskById(int theId) {
        Optional<Task> task=taskDAORepository.findById(theId);
        Task theTask=null;
        if(task.isPresent()){
       theTask   =task.get();
        }
        else{
            throw new RuntimeException("Did not find Task id - " + theId);
        }
        return theTask;
    }

    @Override
    public Task findTaskByUserId(String userId) {
        return taskDAORepository.findByUserId_UserId(userId);
    }
}
