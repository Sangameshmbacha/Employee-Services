package com.example.entity;

import java.time.LocalDate;

import com.example.enums.EmploymentMode;
import com.example.enums.EmploymentStatus;
import com.example.enums.EmploymentType;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "employment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Employment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private EmploymentType employmentType;

    @Enumerated(EnumType.STRING)
    private EmploymentMode mode;

    private LocalDate dateOfJoining;
    private Integer probationPeriodMonths;
    private Integer managerId;

    @Enumerated(EnumType.STRING)
    private EmploymentStatus status;

    private Boolean isActive;

    @OneToOne
    @JoinColumn(
        name = "employee_id",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_employment_employee")
    )
    private Employee employee;
}
