package com.hibernate_concepts.spring_jpa_mapping.repository;

import com.hibernate_concepts.spring_jpa_mapping.models.Comment;
import com.hibernate_concepts.spring_jpa_mapping.models.Tutorial;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Tutorial> findByTutorialId(Long postId);

    @Transactional
    void deleteByTutorialId(long tutorialId);
}
