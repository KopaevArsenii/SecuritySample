package com.kopaev.SecuritySample.Services;

import com.kopaev.SecuritySample.models.Task;
import com.kopaev.SecuritySample.repositories.TaskRepository;
import com.kopaev.SecuritySample.models.User;
import com.kopaev.SecuritySample.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    public List<Task> getUserTasks(String token) {
        final String username = jwtService.extractUsername(token);
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return user.getTasks();
    }

    public void deleteTaskById(int taskId) throws Exception{
        Optional<Task> optionalTask = taskRepository.findById(taskId);
        if(optionalTask.isPresent()){
            taskRepository.deleteById(taskId);
        } else {
            throw new Exception("Task with ID " + taskId + " not found");
        }

    }

    public Optional<Task> getTaskById(int id) {
        return taskRepository.findById(id);
    }
    public void saveNewTask(Task task){
        taskRepository.save(task);
    }

}
