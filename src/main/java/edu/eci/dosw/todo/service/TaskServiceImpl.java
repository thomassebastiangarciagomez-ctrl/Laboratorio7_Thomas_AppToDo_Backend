package edu.eci.dosw.todo.service;
import java.time.LocalDateTime;
import java.util.*;
import edu.eci.dosw.todo.dto.TaskCreateRequest;
import edu.eci.dosw.todo.dto.TaskResponse;
import edu.eci.dosw.todo.dto.TaskUpdateRequest;
import edu.eci.dosw.todo.entity.TaskEntity;
import edu.eci.dosw.todo.entity.TaskPriority;
import edu.eci.dosw.todo.entity.TaskStatus;
import edu.eci.dosw.todo.exception.TaskNotFoundException;
import edu.eci.dosw.todo.repository.TaskRepository;
import org.springframework.stereotype.Service;

@Service
public class TaskServiceImpl implements TaskService{
    
    private TaskRepository taskRepository;

    public TaskServiceImpl(TaskRepository taskRepository){
        this.taskRepository = taskRepository;
    }

    /**
     * 
     * @see edu.eci.dosw.todo.service.TaskService#findAll()
     */
    @Override
    public List<TaskResponse> findAll(){
        return taskRepository.findAll().stream()
                .map(entity -> TaskResponse.fromEntity(entity))
                .toList();
    }

    /**
     * 
     * @see edu.eci.dosw.todo.service.TaskService#findById(java.lang.Long)
     */
    @Override
    public TaskResponse findById(Long id){
        TaskEntity entity = taskRepository.findById(id)
                 .orElseThrow(() -> new TaskNotFoundException(id));
        return TaskResponse.fromEntity(entity);
    }

    /**
     * 
     * @see edu.eci.dosw.todo.service.TaskService#create(edu.eci.dosw.todo.dto.TaskCreateRequest)
     */
    @Override
    public TaskResponse create(TaskCreateRequest request){
        TaskEntity entity = new TaskEntity();
        entity.setTitle(request.getTitle());
        entity.setDescription(request.getDescription());
        entity.setStatus(TaskStatus.PENDING);
        entity.setPriority(request.getPriority() != null ? request.getPriority() : TaskPriority.MEDIUM);
        entity.setDueDate(request.getDueDate());
        entity.setCreatedAt(LocalDateTime.now());

        TaskEntity saved = taskRepository.save(entity);
        return TaskResponse.fromEntity(saved);
    }

    /**
     * 
     * @see edu.eci.dosw.todo.service.TaskService#update(java.lang.Long, edu.eci.dosw.todo.dto.TaskUpdateRequest)
     */
    @Override
    public TaskResponse update(Long id, TaskUpdateRequest request) {
        TaskEntity entity = taskRepository.findById(id)
                 .orElseThrow(() -> new TaskNotFoundException(id));
        entity.setTitle(request.getTitle());
        entity.setDescription(request.getDescription());
        entity.setStatus(request.getStatus());
        entity.setPriority(request.getPriority());
        entity.setDueDate(request.getDueDate());
        TaskEntity updated = taskRepository.save(entity);
        return TaskResponse.fromEntity(updated);
    }

    /**
     * 
     * @see edu.eci.dosw.todo.service.TaskService#delete(java.lang.Long)
     */
    @Override
    public void delete(Long id){
         TaskEntity entity = taskRepository.findById(id)
                 .orElseThrow(() -> new TaskNotFoundException(id));
        taskRepository.delete(entity);
    }
}
