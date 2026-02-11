package com.example.dto;

import java.time.LocalDate;

import com.example.enums.EmploymentStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmploymentRequestDTO {

    @NotBlank
    private String employmentType;

    @NotBlank
    private String mode;

    @NotNull
    @PastOrPresent(message = "Date of joining cannot be in future")
    private LocalDate dateOfJoining;
    
    private Integer probationPeriodMonths;
    private Integer managerId;
    private EmploymentStatus status;
    private Boolean isActive;
   
}
