package com.example.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeRequestDTO {



    @NotNull
    private PersonalInfoDTO personalInfo;

    @NotNull
    private EmploymentDetailsDTO employmentDetails;

    private List<SkillRequestDTO> skills;
    private List<ProjectRequestDTO> projects;

    private StatusDTO status;
    private AuditDTO audit;
    private AddressDTO Addressdto;
}
