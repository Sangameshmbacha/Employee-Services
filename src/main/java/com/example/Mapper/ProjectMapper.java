package com.example.Mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.dto.ProjectResponseDTO;
import com.example.entity.EmployeeProject;

@Mapper(componentModel = "spring")
public interface ProjectMapper {

    @Mapping(source = "project.projectId", target = "projectId")
    @Mapping(source = "project.projectName", target = "projectName")
    @Mapping(source = "role", target = "role")
    @Mapping(source = "allocationPercentage", target = "allocationPercentage")
    ProjectResponseDTO toDto(EmployeeProject employeeProject);
}