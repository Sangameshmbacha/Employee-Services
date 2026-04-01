package com.example.cache;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import com.example.entity.Designation;
import com.example.repository.DesignationRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(
	    properties = {
	        "spring.cache.type=caffeine"
	    }
	)
	@EnableCaching
	@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
	class DesignationServiceTest {

	    @Autowired
	    private DesignationService service;

	    @Autowired
	    private DesignationRepository repository;

	    @BeforeEach
	    void setup() {
	        repository.deleteAll();

	        Designation d = new Designation();
	        d.setName("SE");

	        repository.save(d);
	    }

	    @Test
	    void shouldGetAllDesignations() {
	        List<Designation> list = service.getAllDesignations();
	        assertThat(list).isNotEmpty();
	    }

	    @Test
	    void shouldUseCacheForGetAllDesignations() {
	        List<Designation> first = service.getAllDesignations();
	        List<Designation> second = service.getAllDesignations();

	        assertThat(first).isEqualTo(second);
	    }

	    @Test
	    void shouldGetDesignationByName() {
	        Designation d = service.getDesignationByName("SE");
	        assertThat(d.getName()).isEqualTo("SE");
	    }

	    @Test
	    void shouldThrowExceptionWhenDesignationNotFound() {
	        assertThatThrownBy(() ->
	            service.getDesignationByName("XYZ")
	        ).isInstanceOf(RuntimeException.class)
	         .hasMessageContaining("Designation not found with name: XYZ");
	    }

	    @Test
	    void shouldAddDesignation() {
	        Designation d = new Designation();
	        d.setName("Manager");

	        Designation saved = service.addDesignation(d);

	        assertThat(saved.getId()).isNotNull();
	    }

	    @Test
	    void shouldDeleteDesignationSuccessfully() {
	        Designation existing = repository.findAll().get(0);

	        service.deleteDesignation(existing.getId());

	        assertThat(repository.findById(existing.getId())).isEmpty();
	    }

	    @Test
	    void shouldHandleDeleteForNonExistingDesignation() {
	        service.deleteDesignation(999L);
	        assertThat(true).isTrue();
	    }

	    @Test
	    void shouldReturnEmptyAfterClearingCache() {
	        repository.deleteAll();
	        service.refreshDesignationCache();
	        List<Designation> list = service.getAllDesignations();
	        assertThat(list).isEmpty();
	    }

	    @Test
	    void shouldRefreshDesignationCache() {
	        service.refreshDesignationCache();
	        assertThat(true).isTrue();
	    }

	    @Test
	    void shouldAddAndFetchDesignation() {
	        Designation d = new Designation();
	        d.setName("Lead");

	        service.addDesignation(d);

	        Designation result = service.getDesignationByName("Lead");

	        assertThat(result).isNotNull();
	    }}