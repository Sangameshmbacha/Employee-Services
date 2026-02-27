package com.example.cache;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.entity.Department;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/departments")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService service;

    @PostMapping
    public ResponseEntity<Department> add(@RequestBody Department department) {
        return ResponseEntity.ok(service.addDepartment(department));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        service.deleteDepartment(id);
        return ResponseEntity.ok("Deleted Successfully");
    }

    @GetMapping
    public ResponseEntity<List<Department>> getAll() {
        return ResponseEntity.ok(service.getAllDepartments());
    }

    @PostMapping("/refresh-cache")
    public ResponseEntity<String> refreshCache() {
        service.refreshDepartmentCache();
        return ResponseEntity.ok("Cache Refreshed");
    }
}
