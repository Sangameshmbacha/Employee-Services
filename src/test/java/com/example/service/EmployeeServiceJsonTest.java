package com.example.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.example.ServiceImpl.EmployeeServiceImpl;
import com.example.Mapper.EmployeeMapper;
import com.example.Validator.EmployeeValidator;
import com.example.cache.DepartmentService;
import com.example.cache.DesignationService;
import com.example.dto.EmployeeRequestDTO;
import com.example.dto.EmployeeResponseDTO;
import com.example.entity.Department;
import com.example.entity.Designation;
import com.example.entity.Employee;
import com.example.repository.EmployeeRepository;
import com.example.repository.ProjectRepository;
import com.example.repository.SkillRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceJsonTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private EmployeeMapper employeeMapper;

    @Mock
    private EmployeeValidator employeeValidator;

    @Mock
    private SkillRepository skillRepository;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private DesignationService designationService;

    @Mock
    private DepartmentService departmentService;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee employee;
    private EmployeeResponseDTO responseDTO;
    private EmployeeRequestDTO requestDTO;

    @BeforeEach
    void setup() throws Exception {

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        InputStream json =
                getClass().getClassLoader()
                        .getResourceAsStream("db/data/employee-request.json");

        requestDTO = mapper.readValue(json, EmployeeRequestDTO.class);

        Department department = new Department();
        department.setId(1L);
        department.setName("IT");

        Designation designation = new Designation();
        designation.setId(1L);
        designation.setName("Software Consultant");

        employee = new Employee();
        employee.setId(1L);
        employee.setFirstName("Shennu");
        employee.setLastName("Patil");
        employee.setEmail("shennu@gmail.com");
        employee.setDateOfBirth(LocalDate.of(2004,4,15));
        employee.setDepartment(department);
        employee.setDesignation(designation);

        responseDTO = new EmployeeResponseDTO();
        responseDTO.setFirstName("Shennu");
        responseDTO.setDepartment("IT");
    }
    @Test
    void shouldCreateEmployeeFromJson() {

        when(departmentService.getDepartmentByName("IT"))
                .thenReturn(employee.getDepartment());

        when(designationService.getDesignationByName("Software Consultant"))
                .thenReturn(employee.getDesignation());

        when(employeeRepository.save(org.mockito.ArgumentMatchers.any(Employee.class)))
                .thenReturn(employee);

        when(employeeMapper.toResponseDto(employee))
                .thenReturn(responseDTO);

        EmployeeResponseDTO result =
                employeeService.createEmployee(requestDTO);

        assertThat(result).isNotNull();
        assertThat(result.getDepartment()).isEqualTo("IT");

        verify(employeeRepository).save(org.mockito.ArgumentMatchers.any(Employee.class));
    }
    @Test
    void shouldReturnEmployeeById() {

        when(employeeRepository.findById(1L))
                .thenReturn(Optional.of(employee));

        when(employeeMapper.toResponseDto(employee))
                .thenReturn(responseDTO);

        EmployeeResponseDTO result =
                employeeService.getEmployeeById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getFirstName()).isEqualTo("Shennu");
    }
}