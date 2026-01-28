package com.example.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectResponseDTO {

    private String projectId;
    private String projectName;
    private String role;
    private Integer allocationPercentage;
}
