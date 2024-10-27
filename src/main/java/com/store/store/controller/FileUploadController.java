package com.store.store.controller;

import com.store.store.entity.Task;
import com.store.store.entity.User;
import com.store.store.service.TaskService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

import java.io.IOException;
import java.nio.file.Paths;

@Controller
public class FileUploadController {

    private final S3Client s3Client;
private  HttpSession session ;
private TaskService taskService ;
    @Value("${aws.s3.bucket}")
    private String bucketName;

@Autowired
    public FileUploadController(S3Client s3Client, HttpSession session, TaskService taskService) {
        this.s3Client = s3Client;
        this.session = session;
        this.taskService = taskService;
    }
    @PostMapping("/upload")
    public String uploadFile(@RequestParam("file") MultipartFile file, Model model) {
        User user = (User) session.getAttribute("user");
        try {
            // Generate unique filename
            String fileName = Paths.get(file.getOriginalFilename()).getFileName().toString();

            // Create S3 PutObjectRequest
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .build();

            // Upload the file to S3
            PutObjectResponse response = s3Client.putObject(putObjectRequest,
                    software.amazon.awssdk.core.sync.RequestBody.fromBytes(file.getBytes()));

            model.addAttribute("messageUpload", "File uploaded successfully: " + fileName);
            user.getTask().setStatus("Completed");
            taskService.saveTask(user.getTask());

        } catch (IOException e) {
            model.addAttribute("messageUpload", "Error uploading file: " + e.getMessage());
            e.printStackTrace();
        }
        if (user != null) {
            if (user.getTask() == null) {
                model.addAttribute("message", "No tasks Available");
            } else {
                model.addAttribute("task", user.getTask());
            }
        } else {
            return "redirect:/showLoginPage";
        }

        return "home";


    }
}

