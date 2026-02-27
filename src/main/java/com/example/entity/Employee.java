package com.example.entity;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.example.enums.EmploymentMode;
import com.example.enums.EmploymentType;
import com.example.enums.Gender;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Entity
@Table(name = "employees")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Employee {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String firstName;

	@Size(min = 2, message = "Last name must be at least 2 characters")
	private String lastName;

	private LocalDate dateOfBirth;

	private String nationality;

	@Email(message = "Invalid email format")
	@NotBlank(message = "Email is required")
	private String email;

    @Enumerated(EnumType.STRING)
    private EmploymentMode mode;
    
    @ManyToOne
    @JoinColumn(name = "designation_id",foreignKey = @ForeignKey(name = "fk_employee_designation"))
    private Designation designation;

    @ManyToOne
    @JoinColumn(name = "department_id",foreignKey = @ForeignKey(name = "fk_employee_department"))
    private Department department;

    @OneToOne(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    private Employment employment;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Address> addresses;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "audit_id",foreignKey = @ForeignKey(name = "fk_employee_audit"))
    private Audit audit;
	private String countryCode;
	private Long phoneNumber;

	@Enumerated(EnumType.STRING)
	private Gender gender;

	@Enumerated(EnumType.STRING)
	private EmploymentType employmentType;

	@OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
	@Builder.Default
	private Set<EmployeeSkill> employeeSkills = new HashSet<>();

	public void addEmployeeSkill(EmployeeSkill employeeSkill) {
		employeeSkills.add(employeeSkill);
		employeeSkill.setEmployee(this);
	}
	@OneToMany(mappedBy = "employee",cascade = CascadeType.ALL,orphanRemoval = true)
	@Builder.Default
	private Set<EmployeeProject> employeeProjects = new HashSet<>();

	public void addEmployeeProject(EmployeeProject employeeProject) {
	    employeeProjects.add(employeeProject);
	    employeeProject.setEmployee(this);
	}
}
