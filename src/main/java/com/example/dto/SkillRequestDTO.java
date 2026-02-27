package com.example.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class SkillRequestDTO {
	
	@NotNull
	private Long id;
	private String name;
	private String level;
	private Integer yearsOfExperience;

}
