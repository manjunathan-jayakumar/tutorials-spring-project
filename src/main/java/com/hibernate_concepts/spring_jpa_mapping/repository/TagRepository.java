package com.hibernate_concepts.spring_jpa_mapping.repository;

import com.hibernate_concepts.spring_jpa_mapping.models.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    List<Tag> findTagsByTutorialsId(Long tutorial);

    Tag findByName(String name);

    boolean existsByName(String tagName);
}
