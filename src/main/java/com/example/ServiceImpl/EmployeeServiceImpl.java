package com.example.ServiceImpl;

import java.util.List;
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
        
        employee.setDateOfJoining(dto.getDateOfJoining());
        employee.setProbationPeriodMonths(dto.getProbationPeriodMonths());
        employee.setManagerId(dto.getManagerId());
        employee.setIsActive(dto.getIsActive());

       
        employee.setGender(Gender.valueOf(dto.getGender().toUpperCase()));
        employee.setEmploymentType(EmploymentType.valueOf(dto.getEmploymentType().toUpperCase()));
        employee.setMode(EmploymentMode.valueOf(dto.getMode().toUpperCase()));


        employee.setDepartment(department);
        employee.setDesignation(designation);

        
        employee.setStatus(EmploymentStatus.ACTIVE);
        employee.setIsActive(true);

        
        if (dto.getAddresses() != null) {
            List<Address> addresses = dto.getAddresses().stream().map(a -> {
                Address address = new Address();
                address.setStreet(a.getStreet());
                address.setCity(a.getCity());
                address.setState(a.getState());
                address.setZipCode(a.getZipCode());
                address.setCountry(a.getCountry());
                address.setAddressType(a.getAddressType());
                address.setEmployee(employee);
                return address;
            }).toList();
            employee.setAddresses(addresses);
        }

        if (dto.getSkills() != null) {
            List<Skill> skills = dto.getSkills().stream().map(s -> {
                Skill skill = new Skill();
                skill.setName(s.getName());
                skill.setLevel(s.getLevel());
                skill.setYearsOfExperience(s.getYearsOfExperience());
                skill.setEmployee(employee);
                return skill;
            }).toList();
            employee.setSkills(skills);
        }

        
        if (dto.getProject() != null) {
            ProjectRequestDTO projectDto = dto.getProject();

            Project project = new Project();
            project.setProjectId(projectDto.getProjectId());
            project.setProjectName(projectDto.getProjectName());
            project.setRole(projectDto.getRole());
            project.setAllocationPercentage(projectDto.getAllocationPercentage());
            project.setEmployee(employee);

            employee.setProjects(List.of(project));
        }


        return EmployeeMapper.toResponseDto(employeeRepository.save(employee));
    }

    @Override
    public EmployeeResponseDTO getEmployeeById(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
        return EmployeeMapper.toResponseDto(employee);
    }

    @Override
    public List<EmployeeResponseDTO> getEmployees(String department, String status, String skills, String project) {
        List<Employee> employees = employeeRepository.findAll(); 

       
        if (skills != null && !skills.isEmpty()) {
            employees = employees.stream()
                    .filter(e -> e.getSkills().stream()
                            .anyMatch(s -> s.getName().equalsIgnoreCase(skills)))
                    .collect(Collectors.toList());
        }
        

        
        if (status != null) {
            employees = employees.stream()
                    .filter(e -> e.getStatus().name().equalsIgnoreCase(status.toString()))
                    .collect(Collectors.toList());
        }

       
        if (department != null && !department.isEmpty()) {
            employees = employees.stream()
                    .filter(e -> e.getDepartment().getName().equalsIgnoreCase(department))
                    .collect(Collectors.toList());
        }
        
        if (project != null && !project.isEmpty()) {
            employees = employees.stream()
                .filter(e -> e.getProjects() != null &&
                             e.getProjects().stream()
                                .anyMatch(p -> p.getProjectId().equalsIgnoreCase(project)))
                .collect(Collectors.toList());
        }


        return employees.stream()
                .map(EmployeeMapper::toResponseDto)
                .collect(Collectors.toList());
    }


    @Override
    public EmployeeResponseDTO updateEmployee(Long id, EmployeeRequestDTO dto) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
        employee.setFirstName(dto.getFirstName());
        employee.setLastName(dto.getLastName());
        return EmployeeMapper.toResponseDto(employeeRepository.save(employee));
    }

    @Override
    public void deleteEmployee(Long id) {
        employeeRepository.deleteById(id);
    }
}
