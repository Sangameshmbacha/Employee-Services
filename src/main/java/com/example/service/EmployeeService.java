package com.example.service;

import java.util.List;

import com.example.dto.EmployeeRequestDTO;
import com.example.dto.EmployeeResponseDTO;
import com.example.enums.EmploymentStatus;

public interface EmployeeService {

    EmployeeResponseDTO createEmployee(EmployeeRequestDTO dto);

    EmployeeResponseDTO getEmployeeById(Long id);

    List<EmployeeResponseDTO> getEmployees(String department, EmploymentStatus status, String Skills, String Project);

    EmployeeResponseDTO updateEmployee(Long id, EmployeeRequestDTO dto);

    void deleteEmployee(Long id);
}
