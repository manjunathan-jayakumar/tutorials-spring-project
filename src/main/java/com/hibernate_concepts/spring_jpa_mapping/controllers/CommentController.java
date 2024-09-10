package com.hibernate_concepts.spring_jpa_mapping.controllers;

import com.hibernate_concepts.spring_jpa_mapping.exceptions.ResourceNotFoundException;
import com.hibernate_concepts.spring_jpa_mapping.models.Comment;
import com.hibernate_concepts.spring_jpa_mapping.repository.CommentRepository;
import com.hibernate_concepts.spring_jpa_mapping.repository.TutorialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/api")
public class CommentController {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    protected TutorialRepository tutorialRepository;

    @PostMapping("/tutorials/{tutorialId}/comments")
    public ResponseEntity<Comment> createComment(@PathVariable("tutorialId") Long tutorialId,
                                                 @RequestBody Comment commentRequest) throws ResourceNotFoundException {
        Comment comment = tutorialRepository.findById(tutorialId).map(tutorial -> {
            commentRequest.setTutorial(tutorial);
            return commentRepository.save(commentRequest);
        }).orElseThrow(() -> new ResourceNotFoundException("tutorial not found with id: "+ tutorialId));

        return new ResponseEntity<>(comment, HttpStatus.CREATED);
    }

    @GetMapping("tutorials/{tutorialId}/comments")
    public ResponseEntity<List<Comment>> getAllCommentsByTutorial(@PathVariable("tutorialId") long tutorialId)
            throws ResourceNotFoundException {
        if(!tutorialRepository.existsById(tutorialId)) {
            throw new ResourceNotFoundException("No tutorial found with Id: "+ tutorialId);
        }
        List<Comment> comments = commentRepository.findByTutorialId(tutorialId);
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

    @GetMapping("/comments/{id}")
    public ResponseEntity<Comment> getCommentById(@PathVariable long id)
            throws ResourceNotFoundException {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No comments found with id: "+ id));

        return new ResponseEntity<>(comment, HttpStatus.OK);
    }
}
