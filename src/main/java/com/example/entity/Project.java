package com.example.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
@Entity
@Table(name = "projects")
@Getter
@Setter
public class Project {

    @Id
    private String projectId;

    private String projectName;
    private String role;
    private Integer allocationPercentage;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;
}
