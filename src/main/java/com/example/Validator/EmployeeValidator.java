package com.example.Validator;

import java.time.LocalDate;
import org.springframework.stereotype.Component;
import com.example.Exception.ResourceAlreadyExistsException;
import com.example.dto.EmployeeRequestDTO;
import com.example.enums.EmploymentMode;
import com.example.enums.EmploymentType;
import com.example.enums.Gender;
import com.example.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EmployeeValidator {

    private final EmployeeRepository employeeRepository;
    public void validateForCreate(EmployeeRequestDTO dto) {

        if (employeeRepository.existsByEmail(dto.getEmail())) {
            throw new ResourceAlreadyExistsException(
                    "Employee already exists with email: " + dto.getEmail());
        }
        validateCommonFields(dto);
    }
    public void validateForUpdate(EmployeeRequestDTO dto) {
        validateCommonFields(dto);
    }
    private void validateCommonFields(EmployeeRequestDTO dto) {

        if (dto.getEmail() == null || 
            !dto.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new IllegalArgumentException("Invalid email format");
        }
        if (dto.getDateOfBirth() != null &&
                dto.getDateOfBirth().isAfter(LocalDate.now())) {
            throw new IllegalArgumentException(
                    "Date of birth cannot be in the future");
        }
        try {
            if (dto.getGender() != null) {
                Gender.valueOf(dto.getGender().toUpperCase());
            }
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Invalid gender value");
        }

        if (dto.getEmployment() != null) {
        	try {
                if (dto.getEmployment().getEmploymentType() != null) {
                    EmploymentType.valueOf(
                            dto.getEmployment().getEmploymentType().toUpperCase());
                }
            } catch (IllegalArgumentException ex) {
                throw new IllegalArgumentException(
                        "Invalid employment type value");
            }
        	try {
                if (dto.getEmployment().getMode() != null) {
                    EmploymentMode.valueOf(
                            dto.getEmployment().getMode().toUpperCase());
                }
            } catch (IllegalArgumentException ex) {
                throw new IllegalArgumentException(
                        "Invalid employment mode value");
            }
            if (dto.getEmployment().getDateOfJoining() != null &&
                dto.getDateOfBirth() != null &&
                dto.getEmployment().getDateOfJoining()
                        .isBefore(dto.getDateOfBirth())) {

                throw new IllegalArgumentException(
                        "Date of joining cannot be before date of birth");
            }
        }
    }
}
