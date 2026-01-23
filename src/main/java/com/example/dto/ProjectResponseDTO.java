package com.example.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ProjectResponseDTO {
    private String projectId;
    private String projectName;
    private String role;
    private Integer allocationPercentage;
}
