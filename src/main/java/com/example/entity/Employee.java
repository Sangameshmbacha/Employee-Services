package com.example.entity;
import java.time.LocalDate;
import java.util.List;

import com.example.enums.EmploymentMode;
import com.example.enums.EmploymentStatus;
import com.example.enums.EmploymentType;
import com.example.enums.Gender;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.ForeignKey;
  
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
    private String lastName;

    private LocalDate dateOfBirth;

    private String nationality;
    private String email;
    private Integer countryCode;
    private Long phoneNumber;

    
    @Enumerated(EnumType.STRING)
    private Gender gender;
    
    @Enumerated(EnumType.STRING)
    private EmploymentType employmentType;

    @Enumerated(EnumType.STRING)
    private EmploymentMode mode;

    private LocalDate dateOfJoining;
    private Integer probationPeriodMonths;
    private Integer managerId;


    @ManyToOne
    @JoinColumn(name = "designation_id", foreignKey = @ForeignKey(name = "fk_employee_designation"))
    
    private Designation designation;

    @ManyToOne
    @JoinColumn(name = "department_id", foreignKey = @ForeignKey(name = "fk_employee_department"))
    private Department department;

    @Enumerated(EnumType.STRING)
    private EmploymentStatus status;

    private Boolean isActive;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Address> addresses;

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Skill> skills;
    
    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Project> projects;

}

