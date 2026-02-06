package com.example.dto;

import java.time.LocalDate;
import java.util.List;

import com.example.enums.Gender;

import lombok.*;

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
    private String countryCode;
    private Long phoneNumber;

    private String designation;
    private String department;

    
    private EmploymentResponseDTO employment;

    private List<AddressDTO> addresses;
    private List<SkillResponseDTO> skills;
    private List<ProjectResponseDTO> projects;
}
