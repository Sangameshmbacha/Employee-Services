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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "addr_seq")
    @SequenceGenerator(
            name = "addr_seq",
            sequenceName = "addr_seq",
            allocationSize = 1
    )
    private Long id;

    private String street;
    private String city;
    private String state;
    private String country;
    private String zipCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;
}


