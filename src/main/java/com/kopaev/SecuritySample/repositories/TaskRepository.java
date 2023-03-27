package com.kopaev.SecuritySample.repositories;

import com.kopaev.SecuritySample.models.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public interface TaskRepository extends JpaRepository<Task, Integer> {

}
