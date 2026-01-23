package com.example.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name ="phones")
@Getter
@Setter

public class Phone {
	
	@Id
	@GeneratedValue
	private Long id;
	
	private String countryCode;
	private String number;
	

}
