package com.example.cache;

import java.util.List;

import com.example.entity.Skill;

public interface SkillService {

    List<Skill> getAllSkills();
    Skill addSkill(Skill skill);
    Skill updateSkill(Long id, Skill skill);
    void deleteSkill(Long id);

    void refreshSkillCache();
}
