package com.example.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    boolean existsByEmail(String email);
    

    @Query("""
            SELECT e FROM Employee e
            JOIN e.employment emp
            WHERE (:departmentId IS NULL OR e.department.id = :departmentId)
            AND (:status IS NULL OR emp.status = :status)
            AND (:dob IS NULL OR e.dateOfBirth = :dob)
            AND (:doj IS NULL OR emp.dateOfJoining = :doj)
            
            AND (:skill IS NULL OR EXISTS (
                SELECT 1 FROM EmployeeSkill es
                JOIN es.skill s
                WHERE es.employee = e
                AND s.name = :skill
            ))
            
            AND (:projectId IS NULL OR EXISTS (
                SELECT 1 FROM EmployeeProject ep
                WHERE ep.employee = e
                AND ep.project.id = :projectId
            ))
            """)
    List<Employee> searchEmployees(
            @Param("departmentId") Long departmentId,
            @Param("status") com.example.enums.EmploymentStatus status,
            @Param("skill") String skill,
            @Param("projectId") Long projectId,
            @Param("dob") LocalDate dob,
            @Param("doj") LocalDate doj
    );
}
