package com.example.cache;

import java.util.List;

import com.example.entity.Designation;

public interface DesignationService {

    Designation addDesignation(Designation designation);

    List<Designation> getAllDesignations();

    Designation getDesignationByName(String name);

    void deleteDesignation(Long id);

    void refreshDesignationCache();
}
