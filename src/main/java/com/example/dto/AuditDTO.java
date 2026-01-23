package com.example.dto;
import java.time.Instant;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class AuditDTO {
    private String createdBy;
    private Instant createdAt;
    private String updatedBy;
    private Instant updatedAt;
}
