package com.example.dto;

import java.time.LocalDate;

import com.example.enums.EmploymentMode;
import com.example.enums.EmploymentStatus;
import com.example.enums.EmploymentType;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class EmploymentResponseDTO {

    private EmploymentType employmentType;
    private EmploymentMode mode;
    private LocalDate dateOfJoining;
    private Integer probationPeriodMonths;
    private Integer managerId;
    private EmploymentStatus status;
    private Boolean isActive;
}
