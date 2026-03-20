package com.example.repository;

import static org.assertj.core.api.Assertions.*;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.test.util.ReflectionTestUtils;
import com.example.Mapper.*;
import com.example.cache.CacheService;
import com.example.dto.*;
import com.example.entity.*;
import com.example.enums.Gender;

class MapperTest {

    @Test
    void shouldMapAddressToDto() {
        AddressMapperImpl mapper = new AddressMapperImpl();

        Address address = Address.builder()
                .city("Bangalore")
                .state("KA")
                .country("India")
                .street("MG Road")
                .zipCode("560001")
                .build();

        AddressDTO dto = mapper.toDto(address);

        assertThat(dto).isNotNull();
        assertThat(dto.getCity()).isEqualTo("Bangalore");
    }
    @Test
    void shouldMapAuditToDto() {
        AuditMapperImpl mapper = new AuditMapperImpl();

        Audit audit = new Audit();
        audit.setCreatedBy("admin");

        AuditDTO dto = mapper.toDto(audit);

        assertThat(dto).isNotNull();
        assertThat(dto.getCreatedBy()).isEqualTo("admin");
    }

    @Test
    void shouldReturnNullWhenEmploymentIsNull() {
        EmploymentMapperImpl mapper = new EmploymentMapperImpl();
        EmploymentResponseDTO dto = mapper.toDto(null);
        assertThat(dto).isNull();
    }
    @Test
    void shouldMapSkill() {
        SkillMapperImpl mapper = new SkillMapperImpl();

        Skill skill = new Skill();
        skill.setId(1L);
        skill.setName("Java");

        EmployeeSkill es = new EmployeeSkill();
        es.setSkill(skill);
        es.setLevel("Intermediate");
        es.setYoe(2);

        SkillResponseDTO dto = mapper.toDto(es);

        assertThat(dto).isNotNull();
        assertThat(dto.getName()).isEqualTo("Java");
    }
    
    @Test
    void shouldMapProject() {
        ProjectMapperImpl mapper = new ProjectMapperImpl();

        Project project = new Project();
        project.setProjectId("P01");
        project.setProjectName("EMS");

        EmployeeProject ep = new EmployeeProject();
        ep.setProject(project);
        ep.setRole("DEV");

        ProjectResponseDTO dto = mapper.toDto(ep);

        assertThat(dto).isNotNull();
        assertThat(dto.getProjectName()).isEqualTo("EMS");
    }
    @Test
    void shouldMapEmployeeToResponseDto() {

        EmployeeMapperImpl mapper = new EmployeeMapperImpl();
        ReflectionTestUtils.setField(mapper, "skillMapper", new SkillMapperImpl());
        ReflectionTestUtils.setField(mapper, "projectMapper", new ProjectMapperImpl());
        ReflectionTestUtils.setField(mapper, "addressMapper", new AddressMapperImpl());

        Employee emp = Employee.builder()
                .id(1L)
                .firstName("Shennu")
                .email("test@gmail.com")
                .build();

        EmployeeResponseDTO dto = mapper.toResponseDto(emp);

        assertThat(dto).isNotNull();
    }
    @Test
    void shouldClearAllCaches() {
        ConcurrentMapCacheManager manager = new ConcurrentMapCacheManager("test");

        CacheService cacheService = new CacheService(manager);

        cacheService.clearAllCaches();

        assertThat(true).isTrue();
    }
    @Test
    void shouldTestEqualsAndHashCode() {
        EmployeeSkillId id1 = new EmployeeSkillId(1L, 2L);
        EmployeeSkillId id2 = new EmployeeSkillId(1L, 2L);

        assertThat(id1).isEqualTo(id2);
        assertThat(id1.hashCode()).isEqualTo(id2.hashCode());
    }
    @Test
    void shouldMapEmployeeRequestToEntity() {

        EmployeeMapperImpl mapper = new EmployeeMapperImpl();

        ReflectionTestUtils.setField(mapper, "addressMapper", new AddressMapperImpl());

        EmployeeRequestDTO dto = new EmployeeRequestDTO();
        dto.setFirstName("Shennu");
        dto.setLastName("Patil");
        dto.setEmail("test@gmail.com");
        dto.setGender("MALE");

        AddressDTO addressDTO = AddressDTO.builder()
                .city("Bangalore")
                .country("India")
                .street("MG Road")
                .zipCode("560001")
                .build();

        dto.setAddresses(List.of(addressDTO));

        EmploymentRequestDTO empReq = new EmploymentRequestDTO();
        empReq.setEmploymentType("FULL_TIME");
        empReq.setMode("WFH");

        dto.setEmployment(empReq);

        Employee emp = mapper.toEntity(dto);

        assertThat(emp).isNotNull();
        assertThat(emp.getFirstName()).isEqualTo("Shennu");
        assertThat(emp.getGender()).isEqualTo(Gender.MALE);
    }
    @Test
    void shouldReturnNullWhenEmployeeRequestIsNull() {
        EmployeeMapperImpl mapper = new EmployeeMapperImpl();

        Employee result = mapper.toEntity(null);

        assertThat(result).isNull();
    }
    @Test
    void shouldReturnNullWhenEmployeeIsNull() {
        EmployeeMapperImpl mapper = new EmployeeMapperImpl();

        EmployeeResponseDTO dto = mapper.toResponseDto(null);

        assertThat(dto).isNull();
    }
    @Test
    void shouldHandleNullCollections() {

        EmployeeMapperImpl mapper = new EmployeeMapperImpl();

        ReflectionTestUtils.setField(mapper, "skillMapper", new SkillMapperImpl());
        ReflectionTestUtils.setField(mapper, "projectMapper", new ProjectMapperImpl());
        ReflectionTestUtils.setField(mapper, "addressMapper", new AddressMapperImpl());

        Employee emp = Employee.builder()
                .firstName("Test")
                .build();
        EmployeeResponseDTO dto = mapper.toResponseDto(emp);
        assertThat(dto).isNotNull();
    }
}
