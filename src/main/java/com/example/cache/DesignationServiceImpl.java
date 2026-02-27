package com.example.cache;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.example.entity.Designation;
import com.example.repository.DesignationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DesignationServiceImpl implements DesignationService {

    @Value("${app.message.fetchDesignation}")
    private String fetchDesignationMessage;

    @Value("${app.message.designationCacheCleared}")
    private String designationCacheClearedMessage;

    private final DesignationRepository repository;

    @Override
    @CachePut(
        value = CacheConstants.DESIGNATION_CACHE,
        key = "#result.id",
        cacheManager = "caffeineCacheManager"
    )
    public Designation addDesignation(Designation designation) {
        return repository.save(designation);
    }

    @Override
    @Cacheable(
        value = CacheConstants.DESIGNATION_CACHE,
        cacheManager = "caffeineCacheManager"
    )
    public List<Designation> getAllDesignations() {

        System.out.println(fetchDesignationMessage);

        return repository.findAll();
    }

    @Override
    @Cacheable(
        value = CacheConstants.DESIGNATION_CACHE,
        key = "#name",
        cacheManager = "caffeineCacheManager"
    )
    public Designation getDesignationByName(String name) {
        return repository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Designation not found with name: " + name));
    }

    @Override
    @CacheEvict(
        value = CacheConstants.DESIGNATION_CACHE,
        key = "#id",
        cacheManager = "caffeineCacheManager"
    )
    public void deleteDesignation(Long id) {
        repository.deleteById(id);
    }

    @Override
    @CacheEvict(
        value = CacheConstants.DESIGNATION_CACHE,
        allEntries = true,
        cacheManager = "caffeineCacheManager"
    )
    public void refreshDesignationCache() {
        System.out.println(designationCacheClearedMessage);
    }
}