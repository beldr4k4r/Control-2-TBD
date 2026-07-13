package com.management.api.repositories;

import java.util.List;

import com.management.api.dto.TaskCountBySectorDTO;
import com.management.api.dto.TaskCountBySectorProjection;
import org.springframework.data.repository.query.Param;
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


    @Query(value = "SELECT s.id_sector AS sectorId, s.sector_name AS sectorName, COUNT(t.id_task) AS taskCount " +
            "FROM tasks t " +
            "JOIN sectors s ON t.id_sector = s.id_sector " +
            "WHERE t.user_id = ?1 " +
            "GROUP BY s.id_sector, s.sector_name " +
            "ORDER BY COUNT(t.id_task) DESC",
            nativeQuery = true)
    List<TaskCountBySectorProjection> countTasksBySectorForUser(Long userId);


}
