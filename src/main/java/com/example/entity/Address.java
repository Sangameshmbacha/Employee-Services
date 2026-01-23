package com.example.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "addresses")
@Getter
@Setter

public class Address {
	@Id
	@GeneratedValue
	private Long id;
	
	private String street;
	private String city;
	private String state;
	private String zipCode;
	private String country;
	private String addressType;
	

}
