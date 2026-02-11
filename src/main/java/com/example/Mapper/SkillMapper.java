package com.example.Mapper;

import org.mapstruct.Mapper;

import com.example.dto.SkillResponseDTO;
import com.example.entity.Skill;

@Mapper(componentModel = "spring")
public interface SkillMapper {
    SkillResponseDTO toDto(Skill skill);
}
