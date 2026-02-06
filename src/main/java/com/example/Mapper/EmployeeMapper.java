package com.example.Mapper;

import java.util.Collections;
import java.util.stream.Collectors;

import com.example.dto.*;
import com.example.entity.Employee;
import com.example.entity.Employment;

public class EmployeeMapper {

    public static EmployeeResponseDTO toResponseDto(Employee employee) {

        if (employee == null) {
            return null;
        }

        Employment emp = employee.getEmployment();

        return EmployeeResponseDTO.builder()

               
                .id(employee.getId())
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .dateOfBirth(employee.getDateOfBirth())
                .gender(employee.getGender())
                .nationality(employee.getNationality())
                .email(employee.getEmail())
                .phoneNumber(employee.getPhoneNumber())
                .countryCode(employee.getCountryCode())

                .designation(employee.getDesignation() != null
                        ? employee.getDesignation().getName()
                        : null)

                .department(employee.getDepartment() != null
                        ? employee.getDepartment().getName()
                        : null)

                .employment(emp != null
                        ? EmploymentResponseDTO.builder()
                            .employmentType(emp.getEmploymentType())
                            .mode(emp.getMode())
                            .dateOfJoining(emp.getDateOfJoining())
                            .probationPeriodMonths(emp.getProbationPeriodMonths())
                            .managerId(emp.getManagerId())
                            .status(emp.getStatus())
                            .isActive(emp.getIsActive())
                            .build()
                        : null)

                
                .addresses(employee.getAddresses() != null
                        ? employee.getAddresses().stream()
                            .map(address -> AddressDTO.builder()
                                .street(address.getStreet())
                                .city(address.getCity())
                                .state(address.getState())
                                .zipCode(address.getZipCode())
                                .country(address.getCountry())
                                .addressType(address.getAddressType())
                                .build())
                            .collect(Collectors.toList())
                        : Collections.emptyList())

              
                .skills(employee.getSkills() != null
                        ? employee.getSkills().stream()
                            .map(skill -> SkillResponseDTO.builder()
                                .name(skill.getName())
                                .level(skill.getLevel())
                                .yearsOfExperience(skill.getYearsOfExperience())
                                .build())
                            .collect(Collectors.toList())
                        : Collections.emptyList())

                
                .projects(employee.getProjects() != null
                        ? employee.getProjects().stream()
                            .map(project -> ProjectResponseDTO.builder()
                                .projectId(project.getProjectId())
                                .projectName(project.getProjectName())
                                .role(project.getRole())
                                .allocationPercentage(project.getAllocationPercentage())
                                .build())
                            .collect(Collectors.toList())
                        : Collections.emptyList())

                .build();
    }
}
