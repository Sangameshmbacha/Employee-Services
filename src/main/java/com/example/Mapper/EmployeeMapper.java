package com.example.Mapper;

import java.util.Collections;
import java.util.stream.Collectors;

import com.example.dto.EmployeeResponseDTO;
import com.example.dto.ProjectResponseDTO;
import com.example.dto.SkillResponseDTO;
import com.example.entity.Employee;

public class EmployeeMapper {

    public static EmployeeResponseDTO toResponseDto(Employee employee) {

        if (employee == null) {
            return null;
        }

        return EmployeeResponseDTO.builder()

                .id(employee.getId())
                .employeeId(employee.getEmployeeId())

                .firstName(
                        employee.getPersonalInfo() != null
                                ? employee.getPersonalInfo().getFirstName()
                                : null
                )
                .lastName(
                        employee.getPersonalInfo() != null
                                ? employee.getPersonalInfo().getLastName()
                                : null
                )
                .dateOfBirth(
                        employee.getPersonalInfo() != null
                                ? employee.getPersonalInfo().getDateOfBirth()
                                : null
                )
                .gender(
                        employee.getPersonalInfo() != null
                                ? employee.getPersonalInfo().getGender()
                                : null
                )
                .nationality(
                        employee.getPersonalInfo() != null
                                ? employee.getPersonalInfo().getNationality()
                                : null
                )

                .email(
                        employee.getPersonalInfo() != null &&
                        employee.getPersonalInfo().getContact() != null
                                ? employee.getPersonalInfo().getContact().getEmail()
                                : null
                )
                .countryCode(
                        employee.getPersonalInfo() != null &&
                        employee.getPersonalInfo().getContact() != null
                                ? employee.getPersonalInfo().getContact().getCountryCode()
                                : null
                )
                .phoneNumber(
                        employee.getPersonalInfo() != null &&
                        employee.getPersonalInfo().getContact() != null
                                ? employee.getPersonalInfo().getContact().getPhoneNumber()
                                : null
                )

               
                .designation(
                        employee.getEmploymentDetails() != null &&
                        employee.getEmploymentDetails().getDesignation() != null
                                ? employee.getEmploymentDetails().getDesignation().getName()
                                : null
                )
                .department(
                        employee.getEmploymentDetails() != null &&
                        employee.getEmploymentDetails().getDepartment() != null
                                ? employee.getEmploymentDetails().getDepartment().getName()
                                : null
                )
                .employmentType(
                        employee.getEmploymentDetails() != null
                                ? employee.getEmploymentDetails().getEmploymentType()
                                : null
                )
                .dateOfJoining(
                        employee.getEmploymentDetails() != null
                                ? employee.getEmploymentDetails().getDateOfJoining()
                                : null
                )
                .probationPeriodMonths(
                        employee.getEmploymentDetails() != null
                                ? employee.getEmploymentDetails().getProbationPeriodMonths()
                                : null
                )
                .managerId(
                        employee.getEmploymentDetails() != null
                                ? employee.getEmploymentDetails().getManagerId()
                                : null
                )
                .office(
                        employee.getEmploymentDetails() != null
                                ? employee.getEmploymentDetails().getOffice()
                                : null
                )
                .mode(
                        employee.getEmploymentDetails() != null
                                ? employee.getEmploymentDetails().getMode()
                                : null
                )

                
                .isActive(employee.getIsActive())
                .status(
                        employee.getStatus() != null
                                ? employee.getStatus().name()
                                : null
                )

                .skills(
                        employee.getSkills() != null
                                ? employee.getSkills()
                                        .stream()
                                        .map(skill -> SkillResponseDTO.builder()
                                                .name(skill.getName())
                                                .level(skill.getLevel())
                                                .yearsOfExperience(skill.getYearsOfExperience())
                                                .build()
                                        )
                                        .collect(Collectors.toList())
                                : Collections.emptyList()
                )

                        .projects(
                        employee.getProjects() != null
                                ? employee.getProjects()
                                        .stream()
                                        .map(project -> ProjectResponseDTO.builder()
                                                .projectId(project.getProjectId())
                                                .projectName(project.getProjectName())
                                                .role(project.getRole())
                                                .allocationPercentage(project.getAllocationPercentage())
                                                .build()
                                        )
                                        .collect(Collectors.toList())
                                : Collections.emptyList()
                )


                .build();
    }
}
