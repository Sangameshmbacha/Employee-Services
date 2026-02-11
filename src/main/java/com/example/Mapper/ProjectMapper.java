package com.example.Mapper;

import org.mapstruct.Mapper;

import com.example.dto.ProjectResponseDTO;
import com.example.entity.Project;

@Mapper(componentModel = "spring")
public interface ProjectMapper {
    ProjectResponseDTO toDto(Project project);
}
