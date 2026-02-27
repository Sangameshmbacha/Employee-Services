package com.example.Mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.example.dto.SkillResponseDTO;
import com.example.entity.EmployeeSkill;
import com.example.entity.Skill;


@Mapper(componentModel = "spring")
public interface SkillMapper {

    @Mapping(source = "skill.id", target = "id")
    @Mapping(source = "skill.name", target = "name")
    @Mapping(source = "level", target = "level")
    @Mapping(source = "yoe", target = "yearsOfExperience")
    SkillResponseDTO toDto(EmployeeSkill employeeSkill);
}