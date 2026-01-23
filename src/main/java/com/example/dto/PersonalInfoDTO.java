package com.example.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class PersonalInfoDTO {

    @NotBlank
    private String firstName;

    private String lastName;
    private LocalDate dateOfBirth;
    private String gender;
    private String nationality;

    @NotNull
    private ContactDTO contact;
}
