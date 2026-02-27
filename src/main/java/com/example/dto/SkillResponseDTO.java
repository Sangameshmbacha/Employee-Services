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
public class SkillResponseDTO {

    private Long id;
    private String name;
    private String level;
    private Integer yearsOfExperience ;
}
