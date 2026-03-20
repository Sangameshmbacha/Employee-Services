package com.example.repository;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.dto.EmployeeRequestDTO;
import com.example.dto.EmploymentRequestDTO;
import com.example.enums.EmploymentMode;
import com.example.enums.EmploymentType;
import com.example.enums.Gender;
import com.example.Exception.ResourceAlreadyExistsException;
import com.example.Validator.EmployeeValidator;

class EmployeeValidatorTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeValidator employeeValidator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void shouldThrowExceptionWhenEmailAlreadyExists() {
        EmployeeRequestDTO dto = new EmployeeRequestDTO();
        dto.setEmail("test@gmail.com");

        when(employeeRepository.existsByEmail("test@gmail.com")).thenReturn(true);

        assertThatThrownBy(() -> employeeValidator.validateForCreate(dto))
                .isInstanceOf(ResourceAlreadyExistsException.class)
                .hasMessageContaining("Employee already exists");
    }
    @Test
    void shouldThrowExceptionForInvalidEmail() {
        EmployeeRequestDTO dto = new EmployeeRequestDTO();
        dto.setEmail("invalid-email");

        assertThatThrownBy(() -> employeeValidator.validateForCreate(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid email format");
    }
    @Test
    void shouldThrowExceptionForFutureDOB() {
        EmployeeRequestDTO dto = new EmployeeRequestDTO();
        dto.setEmail("test@gmail.com");
        dto.setDateOfBirth(LocalDate.now().plusDays(1));

        when(employeeRepository.existsByEmail(dto.getEmail())).thenReturn(false);

        assertThatThrownBy(() -> employeeValidator.validateForCreate(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Date of birth cannot be in the future");
    }
    @Test
    void shouldThrowExceptionForInvalidGender() {
        EmployeeRequestDTO dto = new EmployeeRequestDTO();
        dto.setEmail("test@gmail.com");
        dto.setGender("INVALID");

        when(employeeRepository.existsByEmail(dto.getEmail())).thenReturn(false);

        assertThatThrownBy(() -> employeeValidator.validateForCreate(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid gender value");
    }
    @Test
    void shouldThrowExceptionForInvalidEmploymentType() {
        EmployeeRequestDTO dto = new EmployeeRequestDTO();
        dto.setEmail("test@gmail.com");

        EmploymentRequestDTO emp = new EmploymentRequestDTO();
        emp.setEmploymentType("WRONG");

        dto.setEmployment(emp);

        when(employeeRepository.existsByEmail(dto.getEmail())).thenReturn(false);

        assertThatThrownBy(() -> employeeValidator.validateForCreate(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid employment type value");
    }
    @Test
    void shouldThrowExceptionForInvalidEmploymentMode() {
        EmployeeRequestDTO dto = new EmployeeRequestDTO();
        dto.setEmail("test@gmail.com");

        EmploymentRequestDTO emp = new EmploymentRequestDTO();
        emp.setMode("WRONG");

        dto.setEmployment(emp);

        when(employeeRepository.existsByEmail(dto.getEmail())).thenReturn(false);

        assertThatThrownBy(() -> employeeValidator.validateForCreate(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Invalid employment mode value");
    }
    @Test
    void shouldThrowExceptionWhenJoiningBeforeDOB() {
        EmployeeRequestDTO dto = new EmployeeRequestDTO();
        dto.setEmail("test@gmail.com");
        dto.setDateOfBirth(LocalDate.of(2000, 1, 1));

        EmploymentRequestDTO emp = new EmploymentRequestDTO();
        emp.setDateOfJoining(LocalDate.of(1999, 1, 1));

        dto.setEmployment(emp);

        when(employeeRepository.existsByEmail(dto.getEmail())).thenReturn(false);

        assertThatThrownBy(() -> employeeValidator.validateForCreate(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Date of joining cannot be before date of birth");
    }
    @Test
    void shouldPassForValidData() {
        EmployeeRequestDTO dto = new EmployeeRequestDTO();
        dto.setEmail("valid@gmail.com");
        dto.setDateOfBirth(LocalDate.of(1990, 1, 1));
        dto.setGender(Gender.MALE.name());

        EmploymentRequestDTO emp = new EmploymentRequestDTO();
        emp.setEmploymentType(EmploymentType.FULL_TIME.name());
        emp.setMode(EmploymentMode.HYBRID.name());
        emp.setDateOfJoining(LocalDate.of(2010, 1, 1));

        dto.setEmployment(emp);

        when(employeeRepository.existsByEmail(dto.getEmail())).thenReturn(false);
        employeeValidator.validateForCreate(dto);
        employeeValidator.validateForUpdate(dto);
    }
}