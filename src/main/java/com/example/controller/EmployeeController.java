package com.example.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.dto.EmployeeRequestDTO;
import com.example.dto.EmployeeResponseDTO;
import com.example.service.EmployeeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
@Tag(
    name = "Employee APIs",
    description = "APIs for managing employee details"
)
public class EmployeeController {

    private final EmployeeService employeeService;

    @Operation(
        summary = "Create a new employee",
        description = "Creates a new employee with personal, employment, skill, and project details"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Employee created successfully",
            content = @Content(schema = @Schema(implementation = EmployeeResponseDTO.class))),
        @ApiResponse(responseCode = "400", description = "Validation error"),
        @ApiResponse(responseCode = "409", description = "Employee already exists")
    })
    @PostMapping("/employee")
    public ResponseEntity<EmployeeResponseDTO> createEmployee(
            @Valid @RequestBody EmployeeRequestDTO dto) {

        return ResponseEntity.ok(employeeService.createEmployee(dto));
    }

    @Operation(
        summary = "Get employee by ID",
        description = "Fetch employee details using employee ID"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Employee found"),
        @ApiResponse(responseCode = "404", description = "Employee not found")
    })
    @GetMapping("/employees/{id}")
    public ResponseEntity<EmployeeResponseDTO> getEmployeeById(
            @Parameter(description = "Employee ID")
            @PathVariable Long id) {

        return ResponseEntity.ok(employeeService.getEmployeeById(id));
    }

    @Operation(
        summary = "Search employees",
        description = "Search employees using optional filters like department, status, skill, project, DOB, and DOJ"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Employees fetched successfully")
    })
    @GetMapping("/employees")
    public ResponseEntity<List<EmployeeResponseDTO>> getEmployees(

            @Parameter(description = "Department name")
            @RequestParam(required = false) Long department,

            @Parameter(description = "Employment status")
            @RequestParam(required = false) String status,

            @Parameter(description = "Skill name")
            @RequestParam(required = false) String skill,

            @Parameter(description = "Project name")
            @RequestParam(required = false) Long project,

            @Parameter(description = "Date of birth")
            @RequestParam(required = false) LocalDate dob,

            @Parameter(description = "Date of joining")
            @RequestParam(required = false) LocalDate doj
    ) {
        return ResponseEntity.ok(
                employeeService.getEmployees(department, status, skill, project, dob, doj)
        );
    }

    @Operation(
        summary = "Update employee",
        description = "Updates existing employee details using employee ID"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Employee updated successfully"),
        @ApiResponse(responseCode = "404", description = "Employee not found"),
        @ApiResponse(responseCode = "400", description = "Validation error")
    })
    @PutMapping("/employees/{id}")
    public ResponseEntity<EmployeeResponseDTO> updateEmployee(
            @Parameter(description = "Employee ID")
            @PathVariable Long id,
            @Valid @RequestBody EmployeeRequestDTO dto) {

        return ResponseEntity.ok(employeeService.updateEmployee(id, dto));
    }

    @Operation(
        summary = "Delete employee",
        description = "Deletes employee using employee ID"
    )
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Employee deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Employee not found")
    })
    @DeleteMapping("/employees/{id}")
    public ResponseEntity<Void> deleteEmployee(
            @Parameter(description = "Employee ID")
            @PathVariable Long id) {

        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    }
}
