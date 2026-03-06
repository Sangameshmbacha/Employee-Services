package com.example.ServiceImpl;

import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.example.Exception.ResourceNotFoundException;
import com.example.Mapper.EmployeeMapper;
import com.example.Validator.EmployeeValidator;
import com.example.cache.DesignationService;
import com.example.dto.EmployeeRequestDTO;
import com.example.dto.EmployeeResponseDTO;
import com.example.entity.*;
import com.example.enums.*;
import com.example.repository.*;
import com.example.service.EmployeeService;
import com.example.cache.DepartmentService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeMapper employeeMapper;
    private final EmployeeValidator employeeValidator;
    private final DepartmentRepository departmentRepository;
    private final SkillRepository skillRepository;
    private final ProjectRepository projectRepository;
    private final DesignationService designationService;
    private final DepartmentService departmentService;
    
    private static final Logger logger =
            LogManager.getLogger(EmployeeServiceImpl.class);


    @Override
    public EmployeeResponseDTO createEmployee(EmployeeRequestDTO dto) {

    	logger.info("createEmployee method ENTERED");
        employeeValidator.validateForCreate(dto);
        
        logger.info("Creating employee with email: {}", dto.getEmail());

        Department department =
                departmentService.getDepartmentByName(dto.getDepartment());

        logger.debug("Department found: {}", department.getName());
        Designation designation =
                designationService.getDesignationByName(dto.getDesignation());

        Employee employee = new Employee();

        employee.setFirstName(dto.getFirstName());
        employee.setLastName(dto.getLastName());
        employee.setDateOfBirth(dto.getDateOfBirth());
        employee.setNationality(dto.getNationality());
        employee.setEmail(dto.getEmail());
        employee.setPhoneNumber(dto.getPhoneNumber());
        employee.setCountryCode(dto.getCountryCode());

        if (dto.getGender() != null) {
            employee.setGender(Gender.valueOf(dto.getGender().toUpperCase()));
        }

        employee.setDepartment(department);
        employee.setDesignation(designation);

        if (dto.getEmployment() != null) {

            Employment employment = new Employment();

            if (dto.getEmployment().getEmploymentType() != null) {
                employment.setEmploymentType(
                        EmploymentType.valueOf(
                                dto.getEmployment().getEmploymentType().toUpperCase()));
            }

            if (dto.getEmployment().getMode() != null) {
                employment.setMode(
                        EmploymentMode.valueOf(
                                dto.getEmployment().getMode().toUpperCase()));
            }

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

        if (dto.getProjects() != null && !dto.getProjects().isEmpty()) {

            dto.getProjects().forEach(p -> {

                Project project = projectRepository
                        .findByProjectId(p.getProjectId())
                        .orElseGet(() -> {
                            Project newProject = new Project();
                            newProject.setProjectId(p.getProjectId());
                            newProject.setProjectName(p.getProjectName());
                            return projectRepository.save(newProject);
                        });

                EmployeeProject employeeProject = new EmployeeProject();
                employeeProject.setEmployee(employee);
                employeeProject.setProject(project);
                employeeProject.setRole(p.getRole());
                employeeProject.setAllocationPercentage(p.getAllocationPercentage());

                employee.addEmployeeProject(employeeProject);
            });
        }

        if (dto.getEmployeeSkills() != null && !dto.getEmployeeSkills().isEmpty()) {

            Set<EmployeeSkill> employeeSkills = new HashSet<>();

            dto.getEmployeeSkills().forEach(s -> {

                Skill skill = skillRepository.findById(s.getId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Skill not found with ID: " + s.getId()));

                EmployeeSkill employeeSkill = new EmployeeSkill();
                employeeSkill.setEmployee(employee);
                employeeSkill.setSkill(skill);
                employeeSkill.setLevel(s.getLevel());
                employeeSkill.setYoe(s.getYearsOfExperience());

                employeeSkills.add(employeeSkill);
            });

            employee.setEmployeeSkills(employeeSkills);
        }

        Audit audit = new Audit();
        audit.setEmployee(employee);
        audit.setCreatedAt(Instant.now());
        audit.setCreatedBy("SYSTEM");
        audit.setLastAction("CREATE");
        employee.setAudit(audit);

        Employee savedEmployee = employeeRepository.save(employee);

        return employeeMapper.toResponseDto(savedEmployee);
    }

    
    @Override
    public List<EmployeeResponseDTO> getEmployees(
            Long department,
            String status,
            String skill,
            Long project,
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
                .map(employeeMapper::toResponseDto)
                .toList();
    }

    @Override
    public EmployeeResponseDTO getEmployeeById(Long id) {

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Employee not found"));

        return employeeMapper.toResponseDto(employee);
    }

    @Override
    public EmployeeResponseDTO updateEmployee(Long id, EmployeeRequestDTO dto) {

        employeeValidator.validateForUpdate(dto);

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Employee not found"));

        employee.setFirstName(dto.getFirstName());
        employee.setLastName(dto.getLastName());
        employee.setEmail(dto.getEmail());
        employee.setNationality(dto.getNationality());
        employee.setPhoneNumber(dto.getPhoneNumber());
        employee.setCountryCode(dto.getCountryCode());

        if (dto.getGender() != null) {
            employee.setGender(Gender.valueOf(dto.getGender().toUpperCase()));
        }

        employee.getAudit().setUpdatedAt(Instant.now());
        employee.getAudit().setUpdatedBy("SYSTEM");
        employee.getAudit().setLastAction("UPDATE");

        Employee updatedEmployee = employeeRepository.save(employee);

        return employeeMapper.toResponseDto(updatedEmployee);
    }

    @Override
    public void deleteEmployee(Long id) {

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Employee not found"));

        if (employee.getAudit() != null) {
            employee.getAudit().markDeleted("SYSTEM");
        }

        employeeRepository.save(employee);
    }
}