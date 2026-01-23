package com.example.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class SkillRequestDTO {
	
	@NotBlank
	private String name;
	
	private String level;
	private Integer yearsOfExperience;

}
