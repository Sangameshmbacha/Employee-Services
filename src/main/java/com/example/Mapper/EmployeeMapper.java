package com.example.Mapper;

import java.util.Collections;
import java.util.stream.Collectors;

import com.example.dto.AddressDTO;
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
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .dateOfBirth(employee.getDateOfBirth())
                .gender(employee.getGender())
                .nationality(employee.getNationality())
                .email(employee.getEmail())
                .phoneNumber(employee.getPhoneNumber())
                .countryCode(employee.getCountryCode())

                
                .employmentType(employee.getEmploymentType())
                .mode(employee.getMode())
                .dateOfJoining(employee.getDateOfJoining())
                .probationPeriodMonths(employee.getProbationPeriodMonths())
                .managerId(employee.getManagerId())

                
                .designation(employee.getDesignation() != null
                        ? employee.getDesignation().getName()
                        : null)

                .department(employee.getDepartment() != null
                        ? employee.getDepartment().getName()
                        : null)

                
                .status(employee.getStatus() != null
                        ? employee.getStatus().name()
                        : null)

                .isActive(employee.getIsActive())

                
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

                
                .projects(
                	    employee.getProjects() != null
                	        ? employee.getProjects().stream()
                	            .map(p -> ProjectResponseDTO.builder()
                	                .projectId(p.getProjectId())
                	                .projectName(p.getProjectName())
                	                .role(p.getRole())
                	                .allocationPercentage(p.getAllocationPercentage())
                	                .build())
                	            .collect(Collectors.toList())
                	        : Collections.emptyList()
                	)

                	.build();
    }
}
