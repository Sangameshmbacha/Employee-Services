package com.example.ServiceImpl;

import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.example.Exception.ResourceNotFoundException;
import com.example.Mapper.EmployeeMapper;
import com.example.Validator.EmployeeValidator;
import com.example.cache.DesignationService;
import com.example.dto.EmployeeRequestDTO;
import com.example.dto.EmployeeResponseDTO;
import com.example.entity.Address;
import com.example.entity.Audit;
import com.example.entity.Department;
import com.example.entity.Designation;
import com.example.entity.Employee;
import com.example.entity.EmployeeProject;
import com.example.entity.Employment;
import com.example.entity.Project;
import com.example.entity.Skill;
import com.example.entity.EmployeeSkill;
import com.example.enums.EmploymentMode;
import com.example.enums.EmploymentStatus;
import com.example.enums.EmploymentType;
import com.example.enums.Gender;
import com.example.repository.DepartmentRepository;
import com.example.repository.EmployeeRepository;
import com.example.repository.ProjectRepository;
import com.example.repository.SkillRepository;
import com.example.entity.ChangeLog;
import com.example.repository.ChangeLogRepository;

import com.example.service.EmployeeService;

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
    private final ChangeLogRepository changeLogRepository;

    private final DesignationService designationService;

    private final ConcurrentHashMap<String, Department> departmentCache = new ConcurrentHashMap<>();

    @Override
    public EmployeeResponseDTO createEmployee(EmployeeRequestDTO dto) {
    	 System.out.println("createEmployee method ENTERED");

        employeeValidator.validateForCreate(dto);

        Department department = departmentCache.computeIfAbsent(
                dto.getDepartment(),
                name -> departmentRepository.findByName(name)
                        .orElseThrow(() ->
                                new ResourceNotFoundException("Department not found"))
        );

        Designation designation = designationService.getDesignationByName(dto.getDesignation());

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
                        EmploymentType.valueOf(dto.getEmployment().getEmploymentType().toUpperCase()));
            }

            if (dto.getEmployment().getMode() != null) {
                employment.setMode(
                        EmploymentMode.valueOf(dto.getEmployment().getMode().toUpperCase()));
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

        Audit audit = new Audit();
        audit.setEmployee(employee);
        employee.setAudit(audit);
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
                                new ResourceNotFoundException("Skill not found with ID: " + s.getId()));

                EmployeeSkill employeeSkill = new EmployeeSkill();
                employeeSkill.setEmployee(employee);
                employeeSkill.setSkill(skill);
                employeeSkill.setLevel(s.getLevel());
                employeeSkill.setYoe(s.getYearsOfExperience());

                employeeSkills.add(employeeSkill);
            });

            employee.setEmployeeSkills(employeeSkills);
        }

        Employee savedEmployee = employeeRepository.save(employee);
        System.out.println("After employee save: " + savedEmployee.getId());
        ChangeLog createLog = new ChangeLog(
                "CREATE",
                savedEmployee.getId(),
                "SYSTEM"
        );
        System.out.println("Before ChangeLog save");
        changeLogRepository.save(createLog);
        System.out.println("After ChangeLog save");

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

        Employee updatedEmployee = employeeRepository.save(employee);
        ChangeLog updateLog = new ChangeLog(
                "UPDATE",
                updatedEmployee.getId(),
                "SYSTEM"
        );
        changeLogRepository.save(updateLog);

        return employeeMapper.toResponseDto(updatedEmployee);
    }

    @Override
    public void deleteEmployee(Long id) {

        if (!employeeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Employee not found");
        }

        employeeRepository.deleteById(id);

        ChangeLog deleteLog = new ChangeLog(
                "DELETE",
                id,
                "SYSTEM"
        );
        changeLogRepository.save(deleteLog);
        
    }
}