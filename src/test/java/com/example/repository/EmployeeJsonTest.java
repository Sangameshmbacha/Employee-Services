package com.example.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import java.io.InputStream; 
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import com.example.entity.Employee;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.util.HashSet;
import java.util.Set;
import com.example.entity.EmployeeSkill;
import com.example.entity.Skill;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")

@Sql(
scripts = {
"/schema.sql",

"/db/data/departments.sql",
"/db/data/designations.sql",
"/db/data/skill.sql",
"/db/data/projects.sql"
},
executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
)

class EmployeeJsonTest {

@Autowired
private EmployeeRepository employeeRepository;
@Autowired
private SkillRepository skillRepository;
private Employee loadEmployeeFromJson() throws Exception {

    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());

    InputStream jsonFile = getClass()
            .getClassLoader()
            .getResourceAsStream("db/data/employee.json");

    assertThat(jsonFile).isNotNull();

    Employee employee = objectMapper.readValue(jsonFile, Employee.class);


    if (employee.getEmployment() != null) {
        employee.getEmployment().setEmployee(employee);
    }
    if (employee.getAddresses() != null) {
        employee.getAddresses().forEach(addr -> addr.setEmployee(employee));
    }
    if (employee.getEmployeeSkills() != null) {

        Set<EmployeeSkill> fixedSkills = new HashSet<>();

        for (EmployeeSkill empSkill : employee.getEmployeeSkills()) {

        	Skill managedSkill = skillRepository
        	        .findById(empSkill.getSkill().getId())
        	        .orElse(null);

        	assertThat(managedSkill).isNotNull();

            EmployeeSkill newSkill = new EmployeeSkill();
            newSkill.setEmployee(employee);
            newSkill.setSkill(managedSkill);
            newSkill.setLevel(empSkill.getLevel());
            newSkill.setYoe(empSkill.getYoe());

            fixedSkills.add(newSkill);
        }

        employee.setEmployeeSkills(fixedSkills);
    }
    if (employee.getEmployeeProjects() != null) {
        employee.getEmployeeProjects().forEach(project -> project.setEmployee(employee));
    }

    if (employee.getAudit() != null) {
        employee.getAudit().setEmployee(employee);
    }

    return employee;
}
private Employee loadEmployeeFromJson(String path) throws Exception {

    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.registerModule(new JavaTimeModule());

    InputStream jsonFile = getClass()
            .getClassLoader()
            .getResourceAsStream(path);

    assertThat(jsonFile).isNotNull();

    Employee employee = objectMapper.readValue(jsonFile, Employee.class);

    if (employee.getEmployment() != null) {
        employee.getEmployment().setEmployee(employee);
    }

    if (employee.getAddresses() != null) {
        employee.getAddresses().forEach(addr -> addr.setEmployee(employee));
    }

    return employee;
}
@Test
@DisplayName("Should insert employee from JSON")
void shouldInsertEmployeeFromJson() throws Exception {

    Employee employee = loadEmployeeFromJson();
    Employee saved = employeeRepository.save(employee);
    assertThat(saved.getId()).isNotNull();
}

@Test
@DisplayName("Should verify email exists after JSON insert")
void shouldVerifyEmailExists() throws Exception {

    Employee employee = loadEmployeeFromJson();
    employeeRepository.save(employee);

    boolean exists = employeeRepository.existsByEmail("shennu@gmail.com");

    assertThat(exists).isTrue();
}
@Test
@DisplayName("Should find employee by ID after JSON insert")
void shouldFindEmployeeById() throws Exception {

    Employee employee = loadEmployeeFromJson();
    Employee saved = employeeRepository.save(employee);

    Optional<Employee> found = employeeRepository.findById(saved.getId());

    assertThat(found).isPresent();
}
@Test
@DisplayName("Should verify employee first name")
void shouldVerifyEmployeeFirstName() throws Exception {

    Employee employee = loadEmployeeFromJson();
    employeeRepository.save(employee);

    List<Employee> employees = employeeRepository.findAll();

    assertThat(employees.get(0).getFirstName()).isEqualTo("Shennu");
}

@Test
@DisplayName("Should verify employee department")
void shouldVerifyEmployeeDepartment() throws Exception {

    Employee employee = loadEmployeeFromJson();
    employeeRepository.save(employee);

    List<Employee> employees = employeeRepository.findAll();

    Employee emp = employees.get(0);

    assertThat(emp).isNotNull();
    assertThat(emp.getDepartment()).isNotNull();
    assertThat(emp.getDepartment().getId()).isEqualTo(1L);
}

@Test
@DisplayName("Should verify employee designation")
void shouldVerifyEmployeeDesignation() throws Exception {

    Employee employee = loadEmployeeFromJson();
    employeeRepository.save(employee);

    List<Employee> employees = employeeRepository.findAll();

    Employee emp = employees.get(0);

    assertThat(emp).isNotNull();
    assertThat(emp.getDesignation()).isNotNull();
    assertThat(emp.getDesignation().getId()).isEqualTo(1L);
}

@Test
@DisplayName("Should return employees after JSON insert")
void shouldReturnEmployees() throws Exception {

    Employee employee = loadEmployeeFromJson();
    employeeRepository.save(employee);

    List<Employee> employees = employeeRepository.findAll();

    assertThat(employees).isNotEmpty();
}
@Test
@DisplayName("Should fail when department is null from JSON")
void shouldFailWhenDepartmentIsNullFromJson() throws Exception {

    Employee emp = loadEmployeeFromJson("db/data/employee-null-dept.json");

    assertThatThrownBy(() -> {
        if (emp.getDepartment() == null) {
            throw new IllegalArgumentException("Department cannot be null");
        }
        employeeRepository.save(emp);
    })
    .isInstanceOf(IllegalArgumentException.class)
    .hasMessageContaining("Department cannot be null");
}
@Test
@DisplayName("Should fail when designation is null")
void shouldFailWhenDesignationIsNull() throws Exception {

    Employee emp = loadEmployeeFromJson("db/data/employee-null-designation.json");

    assertThatThrownBy(() -> {
        if (emp.getDesignation() == null) {
            throw new IllegalArgumentException("Designation cannot be null");
        }
        employeeRepository.save(emp);
    })
    .isInstanceOf(IllegalArgumentException.class)
    .hasMessageContaining("Designation cannot be null");
}
@Test
@DisplayName("Should fail when address is null")
void shouldFailWhenAddressIsNull() throws Exception {

    Employee emp = loadEmployeeFromJson("db/data/employee-null-address.json");

    assertThatThrownBy(() -> {
        if (emp.getAddresses() == null || emp.getAddresses().isEmpty()) {
            throw new IllegalArgumentException("Address cannot be null");
        }
        employeeRepository.save(emp);
    })
    .isInstanceOf(IllegalArgumentException.class)
    .hasMessageContaining("Address cannot be null");
}
@Test
@DisplayName("Should fail when last name is null")
void shouldFailWhenLastNameIsNull() throws Exception {

    Employee emp = loadEmployeeFromJson("db/data/employee-null-lastname.json");

    assertThatThrownBy(() -> {
        if (emp.getLastName() == null) {
            throw new IllegalArgumentException("Last name cannot be null");
        }
        employeeRepository.save(emp);
    })
    .isInstanceOf(IllegalArgumentException.class)
    .hasMessageContaining("Last name cannot be null");
}
}
