package com.example.Mapper;

import org.mapstruct.Mapper;

import com.example.dto.AddressDTO;
import com.example.entity.Address;

@Mapper(componentModel = "spring")
public interface AddressMapper {
    AddressDTO toDto(Address address);
}
