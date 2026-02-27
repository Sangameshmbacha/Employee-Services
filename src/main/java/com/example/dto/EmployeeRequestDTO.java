package com.example.dto;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeRequestDTO {

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotNull
    @Past(message = "Date of birth must be in past")
    private LocalDate dateOfBirth;

    @NotBlank
    private String gender;

    @NotBlank
    private String nationality;

    @Email
    @NotBlank
    private String email;

    @NotNull
    @Positive
    private Long phoneNumber;

    @NotNull
    private String countryCode;

    @NotBlank
    private String department;

    @NotBlank
    private String designation;
    
    @NotNull
    private EmploymentRequestDTO employment;

    private List<AddressDTO> addresses;
    private List<SkillRequestDTO>employeeSkills;
    private List<ProjectRequestDTO> projects;
    
    
}
