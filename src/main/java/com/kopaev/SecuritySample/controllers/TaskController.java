package com.kopaev.SecuritySample.controllers;

import com.kopaev.SecuritySample.DTO.Request.AddTaskRequest;
import com.kopaev.SecuritySample.DTO.Response.SuccessResponse;
import com.kopaev.SecuritySample.DTO.Response.TaskResponse;
import com.kopaev.SecuritySample.Exceptions.NotFoundException;
import com.kopaev.SecuritySample.models.Task;
import com.kopaev.SecuritySample.Services.TaskService;
import com.kopaev.SecuritySample.DTO.Response.ErrorResponse;
import com.kopaev.SecuritySample.Services.JwtService;
import com.kopaev.SecuritySample.models.User;
import com.kopaev.SecuritySample.repositories.UserRepository;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class TaskController {
    private final TaskService taskService;
    private final JwtService jwtService;
    private final UserRepository userRepository;

    @GetMapping("/getAll")
    public ResponseEntity<?> getAllTasks(@RequestHeader("Authorization") String token) {
        try {
            final String jwt = token.substring(7); // remove "Bearer " prefix
            List<Task> list = taskService.getUserTasks(jwt);

            List<TaskResponse> response = new ArrayList<>();
            for (Task task : list){
                TaskResponse newItem = TaskResponse.builder()
                        .id(task.getId())
                        .description(task.getDescription())
                        .isDone(task.isDone())
                        .name(task.getName())
                        .build();
                response.add(newItem);
            }

            return ResponseEntity.ok().body(response);
        } catch(ExpiredJwtException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponse("JWT token expired")); //TODO
        }
    }

    @PostMapping("/addTask")
    public ResponseEntity<?> addTask(@RequestHeader("Authorization") String token,
                                     @RequestBody AddTaskRequest request){
        try {
            final String jwt = token.substring(7); // remove "Bearer " prefix
            String username = jwtService.extractUsername(jwt);
            User user = userRepository.findByUsername(username).orElse(null);

            Task newTask = Task.builder()
                    .description(request.getDescription())
                    .isDone(request.isDone())
                    .name(request.getName())
                    .user(user).build();

            taskService.saveNewTask(newTask);
            return ResponseEntity.ok().body(new SuccessResponse("Task added successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    @DeleteMapping("/deleteTask")
    public ResponseEntity<?> deleteTask(@RequestParam("taskId") int taskId) {
        try {
            taskService.deleteTaskById(taskId);
            return ResponseEntity.ok().body(new SuccessResponse("Task deleted successfully"));
        } catch(Exception e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        }
    }

    @PatchMapping("/makeDone")
    public ResponseEntity<?> makeDone(@RequestParam("taskId") int taskId) {
        try {

            Task task = taskService.getTaskById(taskId).orElse(null);
            if (task != null) {
                System.out.println(task.isDone());
                task.setDone(!task.isDone());
                System.out.println(task.isDone());
                taskService.saveNewTask(task);
                return ResponseEntity.ok().body(new SuccessResponse("Task was updated"));
            } else {
                throw new NotFoundException("Task with id " + taskId + " wasn't found");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}

