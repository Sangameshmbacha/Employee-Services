package com.example.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.entity.Designation;

public interface DesignationRepository extends JpaRepository<Designation, Long> {

    Optional<Designation> findByName(String name);
}
