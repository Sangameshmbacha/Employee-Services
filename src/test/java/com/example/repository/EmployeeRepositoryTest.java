package com.example.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
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

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@ActiveProfiles("test")

@Sql(
scripts = {
"/schema.sql",

"/db/data/departments.sql",
"/db/data/designations.sql",
"/db/data/skill.sql",
"/db/data/projects.sql",

"/db/data/employees.sql",

"/db/data/addresses.sql",
"/db/data/employment.sql",

"/db/data/employee_skills.sql",
"/db/data/employee_projects.sql",

"/db/data/audit_logs.sql"
},
executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD
)
class EmployeeRepositoryTest {

@Autowired
private EmployeeRepository employeeRepository;
@Test
@DisplayName("Should find employee by ID")
void shouldFindEmployeeById() {
    Optional<Employee> employee = employeeRepository.findById(1L);

    assertThat(employee).isPresent();
    assertThat(employee.get().getFirstName()).isEqualTo("Shennu");
    assertThat(employee.get().getDepartment().getName()).isEqualTo("IT");
    assertThat(employee.get().getDesignation().getName()).isEqualTo("Software Consultant");
}
@Test
@DisplayName("Should return true if email exists")
void shouldReturnTrueIfEmailExists() {
    boolean exists = employeeRepository.existsByEmail("shennu@gmail.com");
    assertThat(exists).isTrue();
}
@Test
@DisplayName("Should return false if email does not exist")
void shouldReturnFalseIfEmailDoesNotExist() {
    boolean exists = employeeRepository.existsByEmail("unknown@gmail.com");
    assertThat(exists).isFalse();
}
@Test
@DisplayName("Should return all employees")
void shouldReturnAllEmployees() {
    List<Employee> employees = employeeRepository.findAll();
    assertThat(employees).isNotNull();
    assertThat(employees.size()).isGreaterThan(0);
}
@Test
@DisplayName("Should search employee by department")
void shouldSearchEmployeeByDepartment() {
    List<Employee> employees = employeeRepository.searchEmployees(
            1L,
            null,
            null,
            null,
            null,
            null
    );
    assertThat(employees).isNotEmpty();
}
@Test
@DisplayName("Should search employee by DOB")
void shouldSearchEmployeeByDOB() {
    List<Employee> employees = employeeRepository.searchEmployees(
            null,
            null,
            null,
            null,
            LocalDate.of(2004, 4, 15),
            null
    );
    assertThat(employees).isNotEmpty();
}
@Test
@DisplayName("Should return 10 employees from H2 database")
void testEmployeeCount() {
    List<Employee> employees = employeeRepository.findAll();
    assertThat(employees).isNotNull();
    assertThat(employees.size()).isEqualTo(10);
}
@Test
@DisplayName("Should return empty when employee ID not found")
void shouldReturnEmptyWhenEmployeeNotFound() {
    Optional<Employee> employee = employeeRepository.findById(999L);
    assertThat(employee).isEmpty();
}
@Test
@DisplayName("Should return empty list for invalid department")
void shouldReturnEmptyForInvalidDepartment() {
    List<Employee> employees = employeeRepository.searchEmployees(
            999L, null, null, null, null, null
    );

    assertThat(employees).isEmpty();
}
@Test
@DisplayName("Should filter by department and DOB")
void shouldFilterByMultipleFields() {
    List<Employee> employees = employeeRepository.searchEmployees(
            1L,
            null,
            null,
            null,
            LocalDate.of(2004, 4, 15),
            null
    );

    assertThat(employees).isNotEmpty();
}
@Test
@DisplayName("Should return empty when skill not found")
void shouldReturnEmptyForInvalidSkill() {

    List<Employee> employees = employeeRepository.searchEmployees(
            null, null, "INVALID_SKILL", null, null, null
    );

    assertThat(employees).isEmpty();
}
@Test
@DisplayName("Should return empty when project not found")
void shouldReturnEmptyForInvalidProject() {

    List<Employee> employees = employeeRepository.searchEmployees(
            null, null, null, 999L, null, null
    );

    assertThat(employees).isEmpty();
}
@Test
@DisplayName("Should return all employees when no filters applied")
void shouldReturnAllWhenNoFilters() {

    List<Employee> employees = employeeRepository.searchEmployees(
            null, null, null, null, null, null
    );

    assertThat(employees).isNotEmpty();
}
@Test
@DisplayName("Should throw exception for duplicate email")
void shouldThrowExceptionForDuplicateEmail() {
    Employee emp = new Employee();
    emp.setFirstName("Test");
    emp.setEmail("shennu@gmail.com");

    assertThatThrownBy(() -> employeeRepository.saveAndFlush(emp))
            .isInstanceOf(Exception.class);
}
@Test
@DisplayName("Should throw exception when email is null")
void shouldThrowExceptionWhenEmailIsNull() {
    Employee emp = new Employee();
    emp.setFirstName("Test");

    assertThatThrownBy(() -> employeeRepository.saveAndFlush(emp))
            .isInstanceOf(Exception.class);
}
@Test
@DisplayName("Should throw exception for invalid department reference")
void shouldThrowExceptionForInvalidForeignKey() {
    Employee emp = new Employee();
    emp.setFirstName("Test");
    emp.setEmail("test123@gmail.com");
    emp.setDepartment(null);

    assertThatThrownBy(() -> employeeRepository.saveAndFlush(emp))
            .isInstanceOf(Exception.class);
}
}

