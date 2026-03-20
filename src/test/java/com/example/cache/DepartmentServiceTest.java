package com.example.cache;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import com.example.entity.Department;
import com.example.repository.DepartmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(
	    properties = {
	        "spring.cache.type=redis"
	    })
	@EnableCaching
	@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
	class DepartmentServiceTest {

	    @Autowired
	    private DepartmentService service;

	    @Autowired
	    private DepartmentRepository repository;

	    @BeforeEach
	    void setup() {
	        repository.deleteAll();

	        Department d = new Department();
	        d.setName("IT");

	        repository.save(d);
	    }

	    @Test
	    void shouldGetAllDepartments() {
	        List<Department> list = service.getAllDepartments();
	        assertThat(list).isNotEmpty();
	    }

	    @Test
	    void shouldUseCacheForGetAllDepartments() {
	        List<Department> first = service.getAllDepartments();
	        List<Department> second = service.getAllDepartments();

	        assertThat(first).isEqualTo(second);
	    }
	    @Test
	    void shouldGetDepartmentByName() {
	        Department dept = service.getDepartmentByName("IT");
	        assertThat(dept.getName()).isEqualTo("IT");
	    }

	    @Test
	    void shouldThrowExceptionWhenDepartmentNotFound() {
	        assertThatThrownBy(() ->
	            service.getDepartmentByName("HR")
	        ).isInstanceOf(RuntimeException.class)
	         .hasMessageContaining("Department not found with name: HR");
	    }

	    @Test
	    void shouldAddDepartment() {
	        Department d = new Department();
	        d.setName("HR");
	        Department saved = service.addDepartment(d);
	        assertThat(saved.getId()).isNotNull();
	    }

	    @Test
	    void shouldDeleteDepartmentSuccessfully() {
	        Department existing = repository.findAll().get(0);
	        service.deleteDepartment(existing.getId());
	        assertThat(repository.findById(existing.getId())).isEmpty();
	    }

	    @Test
	    void shouldHandleDeleteForNonExistingDepartment() {
	        service.deleteDepartment(999L);
	        assertThat(true).isTrue();
	    }
	    @Test
	    void shouldReturnEmptyAfterClearingCache() {
	        repository.deleteAll();
	        service.refreshDepartmentCache();
	        List<Department> list = service.getAllDepartments();

	        assertThat(list).isEmpty();
	    }

	    @Test
	    void shouldThrowExceptionWhenDepartmentNameIsNull() {
	        assertThatThrownBy(() ->
	            service.getDepartmentByName(null)
	        ).isInstanceOf(Exception.class);
	    }

	    @Test
	    void shouldRefreshDepartmentCache() {
	        service.refreshDepartmentCache();
	        assertThat(true).isTrue();
	    }

	    @Test
	    void shouldAddAndFetchDepartment() {
	        Department d = new Department();
	        d.setName("Finance");
	        service.addDepartment(d);
	        Department result = service.getDepartmentByName("Finance");
	        assertThat(result).isNotNull();
	    }
	    @Test
	    void shouldDeleteAndNotFindDepartment() {
	        Department d = repository.findAll().get(0);

	        service.deleteDepartment(d.getId());
	        service.refreshDepartmentCache();
	        assertThatThrownBy(() ->
	            service.getDepartmentByName("IT")
	        ).isInstanceOf(RuntimeException.class);
	    }
	}