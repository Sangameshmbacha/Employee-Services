package com.example.ServiceImpl;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.Exception.ResourceAlreadyExistsException;
import com.example.Exception.ResourceNotFoundException;
import com.example.Mapper.EmployeeMapper;
import com.example.dto.EmployeeRequestDTO;
import com.example.dto.EmployeeResponseDTO;
import com.example.dto.ProjectRequestDTO;
import com.example.entity.Address;
import com.example.entity.Department;
import com.example.entity.Designation;
import com.example.entity.Employee;
import com.example.entity.Employment;
import com.example.entity.Project;
import com.example.entity.Skill;
import com.example.enums.EmploymentMode;
import com.example.enums.EmploymentStatus;
import com.example.enums.EmploymentType;
import com.example.enums.Gender;
import com.example.repository.DepartmentRepository;
import com.example.repository.DesignationRepository;
import com.example.repository.EmployeeRepository;
import com.example.service.EmployeeService;


import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor

public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final DesignationRepository designationRepository;
    
    
    @Override
    public EmployeeResponseDTO createEmployee(EmployeeRequestDTO dto) {
    	

        if (employeeRepository.existsByEmail(dto.getEmail())) {
            throw new ResourceAlreadyExistsException(
                    "Employee already exists with email: " + dto.getEmail());
        }

        if (dto.getEmployment() != null
                && dto.getEmployment().getDateOfJoining() != null
                && dto.getEmployment().getDateOfJoining().isBefore(dto.getDateOfBirth())) {
            throw new IllegalArgumentException(
                    "Date of joining cannot be before date of birth");
        }

        Department department = departmentRepository
                .findByName(dto.getDepartment())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found"));

        Designation designation = designationRepository
                .findByName(dto.getDesignation())
                .orElseThrow(() -> new ResourceNotFoundException("Designation not found"));

       
        Employee employee = new Employee();
        employee.setFirstName(dto.getFirstName());
        employee.setLastName(dto.getLastName());
        employee.setDateOfBirth(dto.getDateOfBirth());
        employee.setNationality(dto.getNationality());
        employee.setEmail(dto.getEmail());
        employee.setPhoneNumber(dto.getPhoneNumber());
        employee.setCountryCode(dto.getCountryCode());
        employee.setGender(Gender.valueOf(dto.getGender().toUpperCase()));
        employee.setDepartment(department);
        employee.setDesignation(designation);

       
        if (dto.getEmployment() != null) {
            Employment employment = new Employment();
            employment.setEmploymentType(
                    EmploymentType.valueOf(dto.getEmployment().getEmploymentType().toUpperCase()));
            employment.setMode(
                    EmploymentMode.valueOf(dto.getEmployment().getMode().toUpperCase()));
            employment.setDateOfJoining(dto.getEmployment().getDateOfJoining());
            employment.setProbationPeriodMonths(dto.getEmployment().getProbationPeriodMonths());
            employment.setManagerId(dto.getEmployment().getManagerId());
            employment.setStatus(dto.getEmployment().getStatus());
            employment.setIsActive(dto.getEmployment().getIsActive());
            

            employment.setEmployee(employee);
            employee.setEmployment(employment);
        }

        if (dto.getAddresses() != null) {
            employee.setAddresses(
                dto.getAddresses().stream().map(a -> {
                    Address address = new Address();
                    address.setStreet(a.getStreet());
                    address.setCity(a.getCity());
                    address.setState(a.getState());
                    address.setZipCode(a.getZipCode());
                    address.setCountry(a.getCountry());
                    address.setAddressType(a.getAddressType());
                    address.setEmployee(employee);
                    return address;
                }).toList()
            );
        }

        if (dto.getSkills() != null && !dto.getSkills().isEmpty()) {

            Set<Skill> skills = dto.getSkills().stream().map(s -> {
                Skill skill = new Skill();
                skill.setName(s.getName());
                skill.setLevel(s.getLevel());
                skill.setYearsOfExperience(s.getYearsOfExperience());
                return skill;
            }).collect(Collectors.toSet());

            employee.setSkills(skills);
        }


        if (dto.getProjects() != null && !dto.getProjects().isEmpty()) {

            Set<Project> projects = new HashSet<>();

            for (ProjectRequestDTO p : dto.getProjects()) {

                Project project = new Project();
                project.setProjectId(p.getProjectId());
                project.setProjectName(p.getProjectName());
                project.setRole(p.getRole());
                project.setAllocationPercentage(p.getAllocationPercentage());

                projects.add(project);
            }

            employee.setProjects(projects);
        }
        return EmployeeMapper.toResponseDto(
                employeeRepository.save(employee)
        );
    }
    
    @Override
    public List<EmployeeResponseDTO> getEmployees(
            String department,
            String status,
            String skill,
            String project,
            LocalDate dob,
            LocalDate doj) {

        EmploymentStatus empStatus =
                status != null ? EmploymentStatus.valueOf(status.toUpperCase()) : null;

        return employeeRepository.searchEmployees(
                department,
                empStatus,
                skill,
                project,
                dob,
                doj
        ).stream()
         .map(EmployeeMapper::toResponseDto)
         .toList();
    }
    
    @Override
    public EmployeeResponseDTO getEmployeeById(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
        return EmployeeMapper.toResponseDto(employee);
    }
   
    @Override
    public EmployeeResponseDTO updateEmployee(Long id, EmployeeRequestDTO dto) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

        employee.setFirstName(dto.getFirstName());
        employee.setLastName(dto.getLastName());
        employee.setEmail(dto.getEmail());

        return EmployeeMapper.toResponseDto(
                employeeRepository.save(employee));
    }
    
    @Override
    public void deleteEmployee(Long id) {
        employeeRepository.deleteById(id);
    }
}
