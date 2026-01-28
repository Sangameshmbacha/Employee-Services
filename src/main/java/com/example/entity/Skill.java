package com.example.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "skill_seq")
    @SequenceGenerator(
        name = "skill_seq",
        sequenceName = "skill_seq",
        allocationSize = 1
    )
    private Long id;

    private String name;
    private String level;
    private Integer yearsOfExperience;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;
}
