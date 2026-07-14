package com.management.api.repositories;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.scheduling.config.Task;

import com.management.api.models.Tasks;

@Repository
public interface TasksRepository extends JpaRepository<Tasks, Long>{

    List<Tasks> findByUserIdAuth(Long userId);
    List<Tasks> findBySector(Long IdSector);

    @Query(value = "SELECT t.* FROM tasks t " + "JOIN sectors s ON t.sector_id = s.id " +
           "JOIN auth_user u ON t.user_id = u.id_auth " +
           "WHERE t.user_id = ?1 AND t.completed = false " +
           "ORDER BY ST_Distance(s.location, u.location) ASC LIMIT 1", 
           nativeQuery = true)
    Tasks findNearestPendingTask(Long userId);

    @Query(value = "SELECT COALESCE(AVG(ST_Distance(s.location, u.location)), 0) " +
           "FROM tasks t " +
           "JOIN sectors s ON t.sector_id = s.id " +
           "JOIN auth_user u ON t.user_id = u.id_auth " +
           "WHERE t.user_id = ?1 AND t.completed = true", 
           nativeQuery = true)
    Double getAverageDistanceOfCompletedTasks(Long userId);

    @Query(value = "SELECT * FROM tasks WHERE " +
            "(?1 IS NULL OR complete_task = ?1) AND " +
            "(?2 IS NULL OR LOWER(task_name) LIKE LOWER(CONCAT('%', ?2, '%')) " +
            "OR LOWER(description) LIKE LOWER(CONCAT('%', ?2, '%')))",
            nativeQuery = true)
    List<Tasks> findByCompletedAndKeyword(Boolean completed, String keyword);
}
