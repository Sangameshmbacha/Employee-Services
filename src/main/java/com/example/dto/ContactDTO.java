package com.example.dto;
import java.util.List;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class ContactDTO {

    @Email
    @NotBlank
    private String email;
    private PhoneDTO phone;
    
    private List<AddressDTO> address;
}
