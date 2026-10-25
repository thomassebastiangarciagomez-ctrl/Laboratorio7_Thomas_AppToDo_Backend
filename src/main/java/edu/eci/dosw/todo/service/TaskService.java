package edu.eci.dosw.todo.service;
import edu.eci.dosw.todo.dto.*;
import java.util.*;

public interface TaskService {
    
    List<TaskResponse> findAll();

    TaskResponse findById(Long Id);

    TaskResponse create(TaskCreateRequest request, Long userId);

    TaskResponse update(Long id, TaskUpdateRequest request);

    void delete(Long id);
}
