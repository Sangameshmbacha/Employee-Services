package com.example.entity;

import java.time.Instant;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "change_logs")
@Getter
@Setter
public class ChangeLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String action; 
    private Long employeeId;

    private Instant timestamp;

    private String performedBy;

    public ChangeLog() {}

    public ChangeLog(String action, Long employeeId, String performedBy) {
        this.action = action;
        this.employeeId = employeeId;
        this.performedBy = performedBy;
        this.timestamp = Instant.now();
    }
}