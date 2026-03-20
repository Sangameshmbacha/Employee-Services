package com.example.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import java.util.List;
import com.example.Exception.DuplicateResourceException;
import com.example.Exception.ResourceAlreadyExistsException;
import com.example.Exception.ResourceNotFoundException;
import com.example.dto.EmployeeResponseDTO;
import com.example.service.EmployeeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(EmployeeController.class)
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;
    @Test
    @DisplayName("Should return 404 when employee not found")
    void shouldHandleResourceNotFoundException() throws Exception {

        when(employeeService.getEmployeeById(1L))
                .thenThrow(new ResourceNotFoundException("Employee not found"));

        mockMvc.perform(get("/api/v1/employees/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Employee not found"));
    }
    @Test
    @DisplayName("Should return 409 for duplicate resource")
    void shouldHandleDuplicateResourceException() throws Exception {

        when(employeeService.getEmployeeById(1L))
                .thenThrow(new DuplicateResourceException("Duplicate resource"));

        mockMvc.perform(get("/api/v1/employees/1"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("Duplicate resource"));
    }
    @Test
    @DisplayName("Should return 409 when resource already exists")
    void shouldHandleResourceAlreadyExistsException() throws Exception {

        when(employeeService.getEmployeeById(1L))
                .thenThrow(new ResourceAlreadyExistsException("Already exists"));

        mockMvc.perform(get("/api/v1/employees/1"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").value("Already exists"));
    }
    @Test
    @DisplayName("Should return 400 for invalid input")
    void shouldHandleIllegalArgumentException() throws Exception {

        when(employeeService.getEmployeeById(1L))
                .thenThrow(new IllegalArgumentException("Invalid input"));

        mockMvc.perform(get("/api/v1/employees/1"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Invalid input"));
    }
    @Test
    @DisplayName("Should return 400 for validation errors")
    void shouldHandleValidationException() throws Exception {

        String invalidJson = "{}";

        mockMvc.perform(post("/api/v1/employee")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson))
                .andExpect(status().isBadRequest());
    }
    @Test
    void shouldGetEmployeeById() throws Exception {

        when(employeeService.getEmployeeById(1L))
                .thenReturn(new EmployeeResponseDTO());

        mockMvc.perform(get("/api/v1/employees/1"))
                .andExpect(status().isOk());
    }
    @Test
    void shouldGetEmployees() throws Exception {

        when(employeeService.getEmployees(null, null, null, null, null, null))
                .thenReturn(List.of(new EmployeeResponseDTO()));

        mockMvc.perform(get("/api/v1/employees"))
                .andExpect(status().isOk());
    }
    @Test
    void shouldDeleteEmployee() throws Exception {

        mockMvc.perform(delete("/api/v1/employees/1"))
                .andExpect(status().isNoContent());
    }
}