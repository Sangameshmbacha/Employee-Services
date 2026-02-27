package com.example.cache;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.example.entity.Skill;
import com.example.repository.SkillRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SkillServiceImpl implements SkillService {

    @Value("${app.message.fetchSkills}")
    private String fetchSkillsMessage;

    @Value("${app.message.skillCacheCleared}")
    private String skillCacheClearedMessage;

    private final SkillRepository repository;

    @Override
    @Cacheable(
        value = CacheConstants.SKILL_CACHE,
        cacheManager = "caffeineCacheManager"
    )
    public List<Skill> getAllSkills() {

        System.out.println(fetchSkillsMessage);

        return repository.findAll();
    }

    @Override
    @CachePut(
        value = CacheConstants.SKILL_CACHE,
        key = "#result.id",
        cacheManager = "caffeineCacheManager"
    )
    public Skill addSkill(Skill skill) {
        return repository.save(skill);
    }
    
    @Override
    @CachePut(
        value = CacheConstants.SKILL_CACHE,
        key = "#id",
        cacheManager = "caffeineCacheManager"
    )
    public Skill updateSkill(Long id, Skill skill) {

        Skill existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Skill not found with id: " + id));

        existing.setName(skill.getName());

        return repository.save(existing);
    }

    @Override
    @CacheEvict(
        value = CacheConstants.SKILL_CACHE,
        key = "#id",
        cacheManager = "caffeineCacheManager"
    )
    public void deleteSkill(Long id) {
        repository.deleteById(id);
    }

    @Override
    @CacheEvict(
        value = CacheConstants.SKILL_CACHE,
        allEntries = true,
        cacheManager = "caffeineCacheManager"
    )
    public void refreshSkillCache() {
        System.out.println(skillCacheClearedMessage);
    }
}