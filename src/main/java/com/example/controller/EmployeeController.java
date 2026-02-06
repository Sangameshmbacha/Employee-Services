package com.example.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.dto.EmployeeRequestDTO;
import com.example.dto.EmployeeResponseDTO;
import com.example.service.EmployeeService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;  

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @PostMapping("/employees")
    public ResponseEntity<EmployeeResponseDTO> createEmployee(
            @Valid @RequestBody EmployeeRequestDTO dto) {
        return ResponseEntity.ok(employeeService.createEmployee(dto));
    }

    @GetMapping("/employees/{id}")
    public ResponseEntity<EmployeeResponseDTO> getEmployeeById(
            @PathVariable Long id) {
        return ResponseEntity.ok(employeeService.getEmployeeById(id));
    }
    
    @GetMapping("/employees")
    public ResponseEntity<List<EmployeeResponseDTO>> getEmployees(
            @RequestParam(required = false) String department,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String skill,
            @RequestParam(required = false) String project,
            @RequestParam(required = false) LocalDate dob,
            @RequestParam(required = false) LocalDate doj
    ) {
        return ResponseEntity.ok(
                employeeService.getEmployees(department, status, skill, project, dob, doj)
        );
    }

  
    @PutMapping("/employees/{id}")
    public ResponseEntity<EmployeeResponseDTO> updateEmployee(
            @PathVariable Long id,
            @Valid @RequestBody EmployeeRequestDTO dto) {
        return ResponseEntity.ok(employeeService.updateEmployee(id, dto));
    }

    @DeleteMapping("/employees/{id}")
    public ResponseEntity<Void> deleteEmployee(
            @PathVariable Long id) {
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }
}
