package com.example.Mapper;

import org.mapstruct.Mapper;

import com.example.dto.EmploymentResponseDTO;
import com.example.entity.Employment;

@Mapper(componentModel = "spring")
public interface EmploymentMapper {
    EmploymentResponseDTO toDto(Employment employment);
}
