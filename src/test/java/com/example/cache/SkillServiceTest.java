package com.example.cache;

import static org.assertj.core.api.Assertions.*;
import java.util.List;
import com.example.entity.EmployeeSkillId;
import com.example.entity.Skill;
import com.example.repository.SkillRepository;
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
class SkillServiceTest {

    @Autowired
    private SkillService service;

    @Autowired
    private SkillRepository repository;

    @BeforeEach
    void setup() {
        repository.deleteAll();

        Skill s = new Skill();
        s.setName("Java");

        repository.save(s);
    }

    @Test
    void shouldGetAllSkills() {
        List<Skill> list = service.getAllSkills();
        assertThat(list).isNotEmpty();
    }
    @Test
    void shouldAddSkill() {
        Skill s = new Skill();
        s.setName("Python");
        Skill saved = service.addSkill(s);
        assertThat(saved.getId()).isNotNull();
    }
    @Test
    void shouldUpdateSkill() {
        Skill existing = repository.findAll().get(0);

        Skill update = new Skill();
        update.setName("Spring");
        Skill result = service.updateSkill(existing.getId(), update);

        assertThat(result.getName()).isEqualTo("Spring");
    }
    @Test
    void shouldThrowExceptionWhenSkillNotFound() {

        assertThatThrownBy(() ->
            service.updateSkill(999L, new Skill())
        ).isInstanceOf(RuntimeException.class)
         .hasMessageContaining("Skill not found with id: 999");
    }
    @Test
    void shouldDeleteSkillSuccessfully() {
        Skill existing = repository.findAll().get(0);
        service.deleteSkill(existing.getId());
        assertThat(repository.findById(existing.getId())).isEmpty();
    }
    @Test
    void shouldHandleDeleteForNonExistingSkill() {
        service.deleteSkill(999L);
        assertThat(true).isTrue();
    }
    @Test
    void shouldThrowExceptionWhenUpdatingWithNull() {
        assertThatThrownBy(() ->
            service.updateSkill(null, new Skill())
        ).isInstanceOf(Exception.class);
    }
    @Test
    void shouldReturnEmptyListWhenNoSkills() {
        repository.deleteAll();
        List<Skill> list = service.getAllSkills();
        assertThat(list).isEmpty();
    }
    @Test
    void shouldTestEmployeeSkillIdEqualsAndHashCode() {

        EmployeeSkillId id1 = new EmployeeSkillId();
        id1.setEmployeeId(1L);
        id1.setSkillId(2L);

        EmployeeSkillId id2 = new EmployeeSkillId();
        id2.setEmployeeId(1L);
        id2.setSkillId(2L);
        assertThat(id1).isEqualTo(id2);

        assertThat(id1.hashCode()).isEqualTo(id2.hashCode());
        id2.setSkillId(3L);
        assertThat(id1).isNotEqualTo(id2);
        assertThat(id1).isNotEqualTo(null);
        assertThat(id1).isNotEqualTo("test");
    }
}