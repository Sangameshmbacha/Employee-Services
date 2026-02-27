package com.example.cache;

import java.util.List;

import com.example.entity.Department;

public interface DepartmentService {

    Department addDepartment(Department department);
    List<Department> getAllDepartments();
    Department getDepartmentByName(String name);
    void deleteDepartment(Long id);
    void refreshDepartmentCache();
}
