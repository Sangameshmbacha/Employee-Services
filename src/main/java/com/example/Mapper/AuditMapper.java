package com.example.Mapper;

import org.mapstruct.Mapper;

import com.example.dto.AuditDTO;
import com.example.entity.Audit;

@Mapper(componentModel = "spring")
public interface AuditMapper {
    AuditDTO toDto(Audit audit);
}
