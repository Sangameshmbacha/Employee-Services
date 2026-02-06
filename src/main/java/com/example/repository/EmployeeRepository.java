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
        SELECT DISTINCT e FROM Employee e
        JOIN e.employment emp
        LEFT JOIN e.skills s
        LEFT JOIN e.projects p
        WHERE (:department IS NULL OR e.department.name = :department)
          AND (:status IS NULL OR emp.status = :status)
          AND (:skill IS NULL OR s.name = :skill)
          AND (:project IS NULL OR p.projectId = :project)
          AND (:dob IS NULL OR e.dateOfBirth = :dob)
          AND (:doj IS NULL OR emp.dateOfJoining = :doj)
    """)
    List<Employee> searchEmployees(
            @Param("department") String department,
            @Param("status") com.example.enums.EmploymentStatus status,
            @Param("skill") String skill,
            @Param("project") String project,
            @Param("dob") LocalDate dob,
            @Param("doj") LocalDate doj
    );
}
