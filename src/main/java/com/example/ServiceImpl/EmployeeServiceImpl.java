package com.example.ServiceImpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.dto.EmployeeRequestDTO;
import com.example.dto.EmployeeResponseDTO;
import com.example.entity.Contact;
import com.example.entity.Department;
import com.example.entity.Designation;
import com.example.entity.Employee;
import com.example.entity.EmploymentDetails;
import com.example.entity.PersonalInfo;
import com.example.entity.Project;
import com.example.entity.Skill;
import com.example.enums.EmploymentStatus;
import com.example.Mapper.EmployeeMapper;
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

        Department department = departmentRepository
                .findByName(dto.getEmploymentDetails().getDepartment())
                .orElseThrow(() -> new RuntimeException("Department not found"));

          Designation designation = designationRepository
                .findByName(dto.getEmploymentDetails().getDesignation())
                .orElseThrow(() -> new RuntimeException("Designation not found"));

            EmploymentDetails employmentDetails = new EmploymentDetails();
        employmentDetails.setDepartment(department);
        employmentDetails.setDesignation(designation);
        employmentDetails.setEmploymentType(dto.getEmploymentDetails().getEmploymentType());
        employmentDetails.setDateOfJoining(dto.getEmploymentDetails().getDateOfJoining());
        employmentDetails.setProbationPeriodMonths(dto.getEmploymentDetails().getProbationPeriodMonths());
        employmentDetails.setManagerId(dto.getEmploymentDetails().getManagerId());

        if (dto.getEmploymentDetails().getWorkLocation() != null) {
            employmentDetails.setOffice(dto.getEmploymentDetails().getWorkLocation().getOffice());
            employmentDetails.setMode(dto.getEmploymentDetails().getWorkLocation().getMode());
        }

          Contact contact = new Contact();
        contact.setEmail(dto.getPersonalInfo().getContact().getEmail());

        if (dto.getPersonalInfo().getContact().getPhone() != null) {
            contact.setCountryCode(dto.getPersonalInfo().getContact().getPhone().getCountryCode());
            contact.setPhoneNumber(dto.getPersonalInfo().getContact().getPhone().getNumber());
        }

        PersonalInfo personalInfo = new PersonalInfo();
        personalInfo.setFirstName(dto.getPersonalInfo().getFirstName());
        personalInfo.setLastName(dto.getPersonalInfo().getLastName());
        personalInfo.setDateOfBirth(dto.getPersonalInfo().getDateOfBirth());
        personalInfo.setGender(dto.getPersonalInfo().getGender());
        personalInfo.setNationality(dto.getPersonalInfo().getNationality());
        personalInfo.setContact(contact);

        Employee employee = new Employee();
        employee.setEmployeeId(dto.getEmployeeId());
        employee.setPersonalInfo(personalInfo);
        employee.setEmploymentDetails(employmentDetails);
        employee.setStatus(EmploymentStatus.ACTIVE);
        employee.setIsActive(
                dto.getStatus() != null ? dto.getStatus().getIsActive() : true
        );

        if (dto.getSkills() != null) {
            List<Skill> skills = dto.getSkills().stream().map(s -> {
                Skill skill = new Skill();
                skill.setName(s.getName());
                skill.setLevel(s.getLevel());
                skill.setYearsOfExperience(s.getYearsOfExperience());
                skill.setEmployee(employee);
                return skill;
            }).collect(Collectors.toList());
            employee.setSkills(skills);
        }

        if (dto.getProjects() != null) {
            List<Project> projects = dto.getProjects().stream().map(p -> {
                Project project = new Project();
                project.setProjectId(p.getProjectId());
                project.setProjectName(p.getProjectName());
                project.setRole(p.getRole());
                project.setAllocationPercentage(p.getAllocationPercentage());
                project.setEmployee(employee);
                return project;
            }).collect(Collectors.toList());
            employee.setProjects(projects);
        }

        Employee savedEmployee = employeeRepository.save(employee);
        return EmployeeMapper.toResponseDto(savedEmployee);
    }

    @Override
    public EmployeeResponseDTO getEmployeeById(Long id) {
        return employeeRepository.findById(id)
                .map(EmployeeMapper::toResponseDto)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
    }

    @Override
    public List<EmployeeResponseDTO> getEmployees(String department, EmploymentStatus status) {

        List<Employee> employees;

        if (department != null) {
            employees = employeeRepository
                    .findByEmploymentDetails_Department_Name(department);
        } else {
            employees = employeeRepository.findAll();
        }

        if (status != null) {
            employees = employees.stream()
                    .filter(e -> e.getStatus() == status)
                    .toList();
        }

        return employees.stream()
                .map(EmployeeMapper::toResponseDto)
                .toList();
    }

    @Override
    public EmployeeResponseDTO updateEmployee(Long id, EmployeeRequestDTO dto) {

        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        employee.getPersonalInfo().setFirstName(dto.getPersonalInfo().getFirstName());
        employee.getPersonalInfo().setLastName(dto.getPersonalInfo().getLastName());
        employee.getPersonalInfo().getContact()
                .setEmail(dto.getPersonalInfo().getContact().getEmail());

        return EmployeeMapper.toResponseDto(employeeRepository.save(employee));
    }

    @Override
    public void deleteEmployee(Long id) {
        employeeRepository.deleteById(id);
    }
}
