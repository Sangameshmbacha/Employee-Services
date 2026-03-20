package com.example.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.example.ServiceImpl.EmployeeServiceImpl;
import com.example.Mapper.EmployeeMapper;
import com.example.Validator.EmployeeValidator;
import com.example.cache.DepartmentService;
import com.example.cache.DesignationService;
import com.example.dto.EmployeeRequestDTO;
import com.example.dto.EmployeeResponseDTO;
import com.example.dto.ProjectRequestDTO;
import com.example.dto.SkillRequestDTO;
import com.example.entity.Audit;
import com.example.entity.Department;
import com.example.entity.Designation;
import com.example.entity.Employee;
import com.example.entity.Project;
import com.example.entity.Skill;
import com.example.repository.DepartmentRepository;
import com.example.repository.EmployeeRepository;
import com.example.repository.ProjectRepository;
import com.example.repository.SkillRepository;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock private EmployeeRepository employeeRepository;
    @Mock private EmployeeMapper employeeMapper;
    @Mock private EmployeeValidator employeeValidator;
    @Mock private DepartmentRepository departmentRepository;
    @Mock private SkillRepository skillRepository;
    @Mock private ProjectRepository projectRepository;
    @Mock private DesignationService designationService;
    @Mock private DepartmentService departmentService;

    @InjectMocks private EmployeeServiceImpl employeeService;

    private Employee employee;
    private EmployeeResponseDTO responseDTO;

    @BeforeEach
    void setup() {
        Department dept = new Department(); dept.setId(1L); dept.setName("IT");
        Designation desig = new Designation(); desig.setId(1L); desig.setName("Software Consultant");

        employee = new Employee();
        employee.setId(1L);
        employee.setFirstName("Shennu");
        employee.setLastName("Patil");
        employee.setEmail("shennu@gmail.com");
        employee.setDateOfBirth(LocalDate.of(2004,4,15));
        employee.setDepartment(dept);
        employee.setDesignation(desig);
        Audit audit = new Audit(); audit.setEmployee(employee); employee.setAudit(audit);

        responseDTO = new EmployeeResponseDTO();
        responseDTO.setFirstName("Shennu");
        responseDTO.setDepartment("IT");
    }

    private EmployeeRequestDTO getBaseDTO() {
        EmployeeRequestDTO dto = new EmployeeRequestDTO();
        dto.setFirstName("Shennu");
        dto.setLastName("Patil");
        dto.setEmail("test@gmail.com");
        dto.setDepartment("IT");
        dto.setDesignation("SE");
        return dto;
    }

    private void mockDeptAndDesignation() {
        Department dept = new Department(); dept.setId(1L); dept.setName("IT");
        Designation desig = new Designation(); desig.setId(1L); desig.setName("SE");
        when(departmentService.getDepartmentByName("IT")).thenReturn(dept);
        when(designationService.getDesignationByName("SE")).thenReturn(desig);
    }

    private void mockSaveAndMapper() {
        when(employeeRepository.save(any())).thenReturn(employee);
        when(employeeMapper.toResponseDto(any())).thenReturn(responseDTO);
    }
    @Test
    void shouldReturnEmployeeById() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(employeeMapper.toResponseDto(employee)).thenReturn(responseDTO);

        EmployeeResponseDTO result = employeeService.getEmployeeById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getFirstName()).isEqualTo("Shennu");
        verify(employeeRepository).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenEmployeeNotFound() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> employeeService.getEmployeeById(1L))
                .isInstanceOf(RuntimeException.class);
    }

    @Test
    void shouldReturnEmployeesList() {
        when(employeeRepository.searchEmployees(null,null,null,null,null,null))
                .thenReturn(List.of(employee));
        when(employeeMapper.toResponseDto(employee)).thenReturn(responseDTO);

        List<EmployeeResponseDTO> result = employeeService.getEmployees(null,null,null,null,null,null);

        assertThat(result).isNotEmpty();
        assertThat(result.size()).isEqualTo(1);
    }

    @Test
    void shouldReturnEmployeesWithFilters() {
        when(employeeRepository.searchEmployees(1L,null,"Java",null,null,null))
                .thenReturn(List.of(employee));
        when(employeeMapper.toResponseDto(employee)).thenReturn(responseDTO);

        List<EmployeeResponseDTO> result = employeeService.getEmployees(1L,null,"Java",null,null,null);
        assertThat(result).isNotEmpty();
    }

    @Test
    void shouldDeleteEmployee() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        employeeService.deleteEmployee(1L);
        verify(employeeRepository).save(employee);
    }

    @Test
    void shouldDeleteEmployeeWithoutAudit() {
        employee.setAudit(null);
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        employeeService.deleteEmployee(1L);
        verify(employeeRepository).save(employee);
    }

    @Test
    void shouldUpdateEmployee() {
        employee.setAudit(new Audit());
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(employeeRepository.save(employee)).thenReturn(employee);
        when(employeeMapper.toResponseDto(employee)).thenReturn(responseDTO);

        EmployeeResponseDTO result = employeeService.updateEmployee(1L, new EmployeeRequestDTO());
        assertThat(result).isNotNull();
        verify(employeeRepository).save(employee);
    }

    @Test
    void shouldThrowWhenUpdatingNonExistingEmployee() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> employeeService.updateEmployee(1L, new EmployeeRequestDTO()))
                .isInstanceOf(RuntimeException.class);
    }
    @Test
    void shouldCreateEmployee() {
        EmployeeRequestDTO dto = getBaseDTO();
        mockDeptAndDesignation();
        mockSaveAndMapper();

        EmployeeResponseDTO result = employeeService.createEmployee(dto);
        assertThat(result).isNotNull();
    }

    @Test
    void shouldCreateEmployeeWithAddresses() {
        EmployeeRequestDTO dto = getBaseDTO();
        var address = new com.example.dto.AddressDTO();
        address.setStreet("Street 1"); address.setCity("Bangalore");
        address.setState("KA"); address.setZipCode("560001"); address.setCountry("India");
        dto.setAddresses(List.of(address));

        mockDeptAndDesignation();
        mockSaveAndMapper();
        assertThat(employeeService.createEmployee(dto)).isNotNull();
    }

    @Test
    void shouldCreateEmployeeWithProjects() {
        EmployeeRequestDTO dto = getBaseDTO();
        ProjectRequestDTO projectDTO = new ProjectRequestDTO();
        projectDTO.setProjectId("P101"); projectDTO.setProjectName("EMS");
        projectDTO.setRole("DEV"); projectDTO.setAllocationPercentage(100);
        dto.setProjects(List.of(projectDTO));

        mockDeptAndDesignation();
        when(projectRepository.findByProjectId("P101")).thenReturn(Optional.empty());
        Project project = new Project(); project.setProjectId("P101");
        when(projectRepository.save(any())).thenReturn(project);
        mockSaveAndMapper();

        assertThat(employeeService.createEmployee(dto)).isNotNull();
        verify(projectRepository).findByProjectId("P101");
        verify(projectRepository).save(any());
    }

    @Test
    void shouldCreateEmployeeWithSkills() {
        EmployeeRequestDTO dto = getBaseDTO();
        SkillRequestDTO skillDTO = new SkillRequestDTO();
        skillDTO.setId(1L); skillDTO.setLevel("Intermediate"); skillDTO.setYearsOfExperience(2);
        dto.setEmployeeSkills(List.of(skillDTO));

        mockDeptAndDesignation();
        when(skillRepository.findById(1L)).thenReturn(Optional.of(new Skill()));
        mockSaveAndMapper();

        assertThat(employeeService.createEmployee(dto)).isNotNull();
    }
}