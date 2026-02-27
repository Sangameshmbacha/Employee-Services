package com.example.cache;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.example.entity.Department;
import com.example.repository.DepartmentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService {

    @Value("${app.message.fetchDepartment}")
    private String fetchDepartmentMessage;

    @Value("${app.message.departmentCacheCleared}")
    private String departmentCacheClearedMessage;

    private final DepartmentRepository repository;

    @Override
    @CachePut(
        value = CacheConstants.DEPARTMENT_CACHE,
        key = "#result.id",
        cacheManager = "redisCacheManager"
    )
    public Department addDepartment(Department department) {
        return repository.save(department);
    }

    @Override
    @Cacheable(
        value = CacheConstants.DEPARTMENT_CACHE,
        cacheManager = "redisCacheManager"
    )
    public List<Department> getAllDepartments() {

        System.out.println(fetchDepartmentMessage);
        return repository.findAll();
    }

    @Override
    @Cacheable(
        value = CacheConstants.DEPARTMENT_CACHE,
        key = "#name",
        cacheManager = "redisCacheManager"
    )
    public Department getDepartmentByName(String name) {

        return repository.findByName(name)
                .orElseThrow(() ->
                        new RuntimeException("Department not found with name: " + name));
    }

    @Override
    @CacheEvict(
        value = CacheConstants.DEPARTMENT_CACHE,
        key = "#id",
        cacheManager = "redisCacheManager"
    )
    public void deleteDepartment(Long id) {
        repository.deleteById(id);
    }

    @Override
    @CacheEvict(
        value = CacheConstants.DEPARTMENT_CACHE,
        allEntries = true,
        cacheManager = "redisCacheManager"
    )
    public void refreshDepartmentCache() {
        System.out.println(departmentCacheClearedMessage);
    }
}
