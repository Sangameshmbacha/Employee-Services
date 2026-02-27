package com.example.Mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.dto.EmployeeRequestDTO;
import com.example.dto.EmployeeResponseDTO;
import com.example.dto.ProjectResponseDTO;
import com.example.dto.SkillResponseDTO;
import com.example.entity.Employee;
import com.example.entity.EmployeeProject;
import com.example.entity.EmployeeSkill;

@Mapper(
	    componentModel = "spring",
	    uses = { SkillMapper.class, ProjectMapper.class, AddressMapper.class }
	)
	public interface EmployeeMapper {

	    @Mapping(target = "designation", ignore = true)
	    @Mapping(target = "department", ignore = true)
	    @Mapping(target = "employeeSkills", ignore = true)
	    Employee toEntity(EmployeeRequestDTO dto);

	    @Mapping(source = "designation.name", target = "designation")
	    @Mapping(source = "department.name", target = "department")
	    @Mapping(source = "employeeSkills", target = "skills")
	    @Mapping(source = "employeeProjects", target = "projects")
	    EmployeeResponseDTO toResponseDto(Employee employee);
	}
