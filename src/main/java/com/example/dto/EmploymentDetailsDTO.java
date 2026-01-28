package com.example.dto;
import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class EmploymentDetailsDTO {

    @NotBlank
    private String designation;

    @NotBlank
    private String department;

    private String employmentType;
    private LocalDate dateOfJoining;
    private Integer probationPeriodMonths;
    private String managerId;
    private WorkLocationDTO workLocation;
    private String AddressDTO;
    
}
