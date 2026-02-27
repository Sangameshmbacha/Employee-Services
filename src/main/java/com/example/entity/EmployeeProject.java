package com.example.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "employee_projects")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeProject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String role;
    private Integer allocationPercentage;

    @ManyToOne
    @JoinColumn(name = "employee_id",
            foreignKey = @ForeignKey(name = "fk_emp_project_employee"))
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "project_id",
            foreignKey = @ForeignKey(name = "fk_emp_project_project"))
    private Project project;
}