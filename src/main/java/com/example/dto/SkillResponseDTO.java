package com.example.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class SkillResponseDTO {
    private String name;
    private String level;
    private Integer yearsOfExperience;
}
