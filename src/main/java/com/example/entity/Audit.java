package com.example.entity;

import java.time.Instant;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "audit_logs")
@Getter
@Setter
public class Audit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String createdBy;
    private Instant createdAt;

    private String updatedBy;
    private Instant updatedAt;

    private String deletedBy;
    private Instant deletedAt;

    private String lastAction; 
    
    @OneToOne
    @JoinColumn(name = "employee_id", nullable = false, unique = true)
    private Employee employee;

    @PrePersist
    public void onCreate() {
        this.createdAt = Instant.now();
        this.createdBy = "SYSTEM";
        this.lastAction = "CREATE";
    }
    public void markUpdated(String user) {
        this.updatedAt = Instant.now();
        this.updatedBy = user;
        this.lastAction = "UPDATE";
    }
    public void markDeleted(String user) {
        this.deletedAt = Instant.now();
        this.deletedBy = user;
        this.lastAction = "DELETE";
    }
}