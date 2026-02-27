package com.example.service;
import java.time.LocalDate;
import java.util.List;

import com.example.dto.EmployeeRequestDTO;
import com.example.dto.EmployeeResponseDTO;

public interface EmployeeService {

    EmployeeResponseDTO createEmployee(EmployeeRequestDTO dto);

    EmployeeResponseDTO getEmployeeById(Long id);
    
    List<EmployeeResponseDTO> getEmployees(
            Long department,
            String status,
            String skill,
            Long project,
            LocalDate dob,
            LocalDate doj
    );

    EmployeeResponseDTO updateEmployee(Long id, EmployeeRequestDTO dto);
    void deleteEmployee(Long id);
}

