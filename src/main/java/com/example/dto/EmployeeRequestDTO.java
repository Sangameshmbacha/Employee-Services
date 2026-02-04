package com.example.dto;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeRequestDTO {

    @NotBlank(message = "firstName must not be blank")
    private String firstName;

    @NotBlank(message = "lastName must not be blank")
    private String lastName;

    @NotNull(message = "dateOfBirth must not be null")
    private LocalDate dateOfBirth;

    @NotBlank(message = "gender must not be blank")
    private String gender;   

    @NotBlank(message = "nationality must not be blank")
    private String nationality;

    @Email(message = "email must be valid")
    @NotBlank(message = "email must not be blank")
    private String email;

    @NotNull(message = "phone must not be null")
    @Positive(message = "phone must be positive")
    private Long phoneNumber;

    @NotNull(message = "countryCode must not be blank")
    private Integer countryCode;

    @NotBlank(message = "department must not be blank")
    private String department;

    @NotBlank(message = "designation must not be blank")
    private String designation;

    @NotBlank(message = "employmentType must not be blank")
    private String employmentType;

    @NotBlank(message = "mode must not be blank")
    private String mode;

    private LocalDate dateOfJoining;
    private Integer probationPeriodMonths;
    private Integer managerId;
    private Boolean isActive;
    private String Audit;

    private List<AddressDTO> addresses;
    private List<SkillRequestDTO> skills;
    private ProjectRequestDTO project;
	
}
