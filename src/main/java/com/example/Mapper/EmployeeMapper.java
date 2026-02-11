package com.example.Mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import com.example.dto.EmployeeRequestDTO;
import com.example.dto.EmployeeResponseDTO;
import com.example.entity.Employee;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

	@Mapping(target = "designation", ignore = true)
	@Mapping(target = "department", ignore = true)
    Employee toEntity(EmployeeRequestDTO dto);
	
	@Mapping(source = "designation.name", target = "designation")
	@Mapping(source = "department.name", target = "department")
    EmployeeResponseDTO toResponseDto(Employee employee);

}
