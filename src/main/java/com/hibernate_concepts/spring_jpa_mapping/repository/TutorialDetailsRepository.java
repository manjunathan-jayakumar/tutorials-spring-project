package com.hibernate_concepts.spring_jpa_mapping.repository;

import com.hibernate_concepts.spring_jpa_mapping.models.TutorialDetails;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TutorialDetailsRepository extends JpaRepository<TutorialDetails, Long> {

    @Transactional
    void deleteById(long id);

    @Transactional
    void deleteByTutorialId(long tutorialId);
}
