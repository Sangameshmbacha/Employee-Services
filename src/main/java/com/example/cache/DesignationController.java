package com.example.cache;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.entity.Designation;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/designations")
@RequiredArgsConstructor
public class DesignationController {

    private final DesignationService service;
    
    @PostMapping
    public ResponseEntity<Designation> add(@RequestBody Designation designation) {
        return ResponseEntity.ok(service.addDesignation(designation));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        service.deleteDesignation(id);
        return ResponseEntity.ok("Deleted Successfully");
    }

    @GetMapping
    public ResponseEntity<List<Designation>> getAll() {
        return ResponseEntity.ok(service.getAllDesignations());
    }

    @PostMapping("/refresh-cache")
    public ResponseEntity<String> refreshCache() {
        service.refreshDesignationCache();
        return ResponseEntity.ok("Cache Refreshed");
    }
}
