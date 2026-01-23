package com.example.entity;

import java.time.LocalDate;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "personal_info")
@Getter
@Setter
public class PersonalInfo {

    @Id
    @GeneratedValue
    private Long id;

    private String firstName;
    private String lastName;

    private LocalDate dateOfBirth;
    private String gender;
    private String nationality;

    @OneToOne(cascade = CascadeType.ALL)
    private Contact contact;
}
