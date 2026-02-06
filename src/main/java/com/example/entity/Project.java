package com.example.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "project")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String projectId;
    private String projectName;
    private String role;
    private Integer allocationPercentage;

    @ManyToMany(mappedBy = "projects")
    private Set<Employee> employees = new HashSet<>();

}
