package com.example.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "addresses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String street;
    private String city;
    private String state;
    private String country;
    private String zipCode;
    private String addressType;

    @ManyToOne
    @JoinColumn(
        name = "employee_id",
        foreignKey = @ForeignKey(name = "fk_addresses_employee")
    )
    private Employee employee;
}



