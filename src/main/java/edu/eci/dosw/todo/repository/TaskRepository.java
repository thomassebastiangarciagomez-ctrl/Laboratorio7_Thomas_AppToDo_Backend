package edu.eci.dosw.todo.repository;
import edu.eci.dosw.todo.entity.TaskEntity;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Long> {
    
    List<TaskEntity> findByUserId(Long userId);

    Optional<TaskEntity> findByIdAndUserId(Long id, Long userId);
    
}