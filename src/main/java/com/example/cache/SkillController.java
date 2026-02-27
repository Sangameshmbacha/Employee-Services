package com.example.cache;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.entity.Skill;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/skills")
@RequiredArgsConstructor
public class SkillController {

    private final SkillService service;

    @PostMapping
    public ResponseEntity<Skill> add(@RequestBody Skill skill) {
        return ResponseEntity.ok(service.addSkill(skill));
    }

    @GetMapping
    public ResponseEntity<List<Skill>> getAll() {
        return ResponseEntity.ok(service.getAllSkills());
    }

    @PostMapping("/refresh-cache")
    public ResponseEntity<String> refreshCache() {
        service.refreshSkillCache();
        return ResponseEntity.ok("Skill Cache Refreshed");
    }
}
