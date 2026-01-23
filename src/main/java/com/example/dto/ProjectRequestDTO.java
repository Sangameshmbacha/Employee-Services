package com.example.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class ProjectRequestDTO {
	
	@NotBlank
	private String projectId;

	private String projectName;
	private String role;
	private Integer allocationPercentage;
	

}
