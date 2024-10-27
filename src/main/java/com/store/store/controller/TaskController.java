package com.store.store.controller;


import com.store.store.entity.Task;
import com.store.store.entity.User;
import com.store.store.service.TaskService;
import com.store.store.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
public class TaskController {
    private UserService userService;
    private TaskService taskService;
    private HttpSession session;
    @Autowired
    public TaskController(UserService userService, TaskService taskService ,HttpSession session) {
        this.userService = userService;
        this.taskService = taskService;
        this.session=session;
    }

    @GetMapping("addTask")
    public String addTask(Model theModel) {
        List<User> users = userService.getAllUsers();
        List<User> usersWithoutTask = new ArrayList<>();
        for (User user : users) {
            if (user.getTask() == null) {
                usersWithoutTask.add(user);
            }
        }
        Task task = new Task();
        theModel.addAttribute("users", usersWithoutTask);
        theModel.addAttribute("task", task);
        return "add-task";
    }

    @Transactional
    @PostMapping("/saveTask")
    public String saveTask(@ModelAttribute("task") Task task) {
        // Check if this is a new task (id = 0) or an existing one (id != 0)
        if (task.getId() != 0) {
            // Existing task, load it from the database
            Task existingTask = taskService.findTaskById(task.getId());
            if (existingTask != null) {
                // Update the existing task with the form data
                existingTask.setName(task.getName());
                existingTask.setPriority(task.getPriority());
                existingTask.setDeadline(task.getDeadline());
                existingTask.setDescription(task.getDescription());
                existingTask.setStatus(task.getStatus());
                existingTask.setUserId(task.getUserId());
                taskService.saveTask(existingTask); // Save the updated task
                User user = (User) session.getAttribute("user");
                user.setTask(existingTask);
                session.setAttribute("user",user);

            } else {
                throw new RuntimeException("Task not found: " + task.getId());
            }
        } else {
            // New task, just save it
            taskService.saveTask(task);
            User user = (User) session.getAttribute("user");
            user.setTask(task);
            session.setAttribute("user",user);
        }

        return "redirect:/taskList";
    }


    @GetMapping("/taskList")
    public String getTasks(Model theModel) {
        List<Task> tasks = taskService.getAllTasks();
        theModel.addAttribute("tasks", tasks);
        return "list-tasks";
    }

    @GetMapping("/deleteTask")
    @Transactional
    public String deleteTask(@RequestParam("taskId") int taskId) {
        Task task = taskService.findTaskById(taskId);
        task.getUserId().setTask(null);
        taskService.deleteTask(task);
        User user = (User) session.getAttribute("user");
        session.setAttribute("user",user);
        user.setTask(null);
        return "redirect:/taskList";
    }

    @GetMapping("/updateTask")
    public String updateTask(Model theModel, @RequestParam("taskId") int taskId) {
        // Find the managed Task entity from the database
        Task task = taskService.findTaskById(taskId);

        if (task == null) {
            return "redirect:/taskList";  // Handle the case where the task is not found
        }

        theModel.addAttribute("task", task);  // Add the managed Task object

        // Fetch users for the dropdown
        List<User> users = userService.getAllUsers();
        List<User> usersWithoutTask = new ArrayList<>();

        for (User user : users) {
            // Allow users who don't have a task or are assigned to the current task being updated
            if (user.getTask() == null || (user.getTask() != null && user.getTask().getId() == (task.getId()))) {
                usersWithoutTask.add(user);
            }
        }
        theModel.addAttribute("users", usersWithoutTask);

        return "add-task";
    }

    @GetMapping("/acceptTask")
    public String acceptTask(@RequestParam("taskId") int taskId ) {
Task theTask=taskService.findTaskById(taskId);
theTask.setStatus("In Progress");
taskService.saveTask(theTask);
        User user = (User) session.getAttribute("user");
        user.setTask(theTask);
        session.setAttribute("user",user);
        return "redirect:/";
    }

}