package com.example.dto;

import java.time.LocalDate;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeResponseDTO {

    private Long id;


    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String gender;
    private String nationality;

   
    private String email;
    private String countryCode;
    private String phoneNumber;

  
    private String designation;
    private String department;
    private String employmentType;
    private LocalDate dateOfJoining;
    private Integer probationPeriodMonths;
    private String managerId;
    private String office;
    private String mode;

    private Boolean isActive;
    private String status;
    
    private List<SkillResponseDTO> skills;
    private List<ProjectResponseDTO> projects;
}
