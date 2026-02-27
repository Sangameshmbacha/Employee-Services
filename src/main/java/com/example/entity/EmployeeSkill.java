package com.example.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "emp_skills")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeSkill {

    @EmbeddedId
    @Builder.Default
    private EmployeeSkillId id = new EmployeeSkillId(); 

    @MapsId("employeeId")
    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @MapsId("skillId")
    @ManyToOne
    @JoinColumn(name = "skill_id")
    private Skill skill;

    private Integer yoe;
    private String level;
}
