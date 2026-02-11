package com.example.dto;

import java.util.List;

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
	private List<ProjectRequestDTO> projects;

}
