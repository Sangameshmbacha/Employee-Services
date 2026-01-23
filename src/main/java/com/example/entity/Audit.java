package com.example.entity;

import java.time.Instant;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "audit_logs")
@Getter
@Setter

public class Audit {
	@Id
	@GeneratedValue
	private Long id;
	
	private String createdBy;
    private Instant createdAt;
    private String updatedBy;
    private Instant updatedAt;

}
