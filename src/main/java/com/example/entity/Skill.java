package com.example.entity;
import java.util.Set;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String level;
    private Integer yearsOfExperience;

    @ManyToMany(mappedBy = "skills")
    private Set<Employee> employees;
}
