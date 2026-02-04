package com.example.dto;

import java.time.LocalDate;
import java.util.List;

import com.example.enums.EmploymentMode;
import com.example.enums.EmploymentType;
import com.example.enums.Gender;

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
    private Gender gender;
    private String nationality;

    private String email;
    private Integer countryCode;
    private Long phoneNumber;

    private String designation;
    private String department;

    private EmploymentType employmentType;
    private EmploymentMode mode;

    private LocalDate dateOfJoining;
    private Integer probationPeriodMonths;
    private Integer managerId;

    private Boolean isActive;
    private String status;

    private List<AddressDTO> addresses;
    private List<SkillResponseDTO> skills;

    private List<ProjectResponseDTO> projects;

}
