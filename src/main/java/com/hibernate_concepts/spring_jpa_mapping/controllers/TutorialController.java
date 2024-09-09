package com.hibernate_concepts.spring_jpa_mapping.controllers;

import com.hibernate_concepts.spring_jpa_mapping.exceptions.ResourceNotFoundException;
import com.hibernate_concepts.spring_jpa_mapping.models.Tutorial;
import com.hibernate_concepts.spring_jpa_mapping.repository.TutorialDetailsRepository;
import com.hibernate_concepts.spring_jpa_mapping.repository.TutorialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class TutorialController {

    @Autowired
    private TutorialRepository tutorialRepository;

    @Autowired
    private TutorialDetailsRepository tutorialDetailsRepository;

    @PostMapping("/tutorials")
    public ResponseEntity<Tutorial> createTutorial(@RequestBody Tutorial tutorial) {
        Tutorial _tutorial = tutorialRepository.save(new Tutorial(
                tutorial.getTitle(), tutorial.getDescription(), tutorial.isPublished()
        ));
        return new ResponseEntity<>(_tutorial, HttpStatus.CREATED);
    }

    @GetMapping("/tutorials")
    public ResponseEntity<List<Tutorial>> getAllTutorials(@RequestParam(required = false)
                                                          String title) {
        List<Tutorial> tutorials = new ArrayList<>();

        if(title == null) {
            tutorials.addAll(tutorialRepository.findAll());
        }
        else {
            tutorials.addAll(tutorialRepository.findByTitleContaining(title));
        }

        if(tutorials.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(tutorials, HttpStatus.OK);
    }
    @DeleteMapping("/tutorials/{id}")
    public ResponseEntity<Void> deleteTutorialById(@PathVariable Long id) throws ResourceNotFoundException {
        Tutorial tutorial = tutorialRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Tutorial not found"));

        if(tutorialDetailsRepository.existsById(id)){
            tutorialDetailsRepository.deleteById(id);
        }
        // The Tutorial and associated TutorialDetails will be deleted automatically due to cascading
        tutorialRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("tutorials")
    public ResponseEntity<Void> deleteAllTutorials() {
        tutorialRepository.deleteAll();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/tutorials/{id}")
    public ResponseEntity<Tutorial> updateTutorial(@PathVariable long id,
                                                   @RequestBody Tutorial tutorial)
            throws ResourceNotFoundException {
        Tutorial _tutorial = tutorialRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Tutorial with id: "+ id+ " not found")
        );
        _tutorial.setTitle(tutorial.getTitle());
        _tutorial.setDescription(tutorial.getDescription());
        _tutorial.setPublished(tutorial.isPublished());

        return new ResponseEntity<>(tutorialRepository.save(_tutorial), HttpStatus.OK);
    }

    @GetMapping("tutorials/{id}")
    public ResponseEntity<Tutorial> getTutorialById(@PathVariable long id)
            throws ResourceNotFoundException {
        Tutorial tutorial = tutorialRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("tutorial not found with id: "+ id)
        );
        return new ResponseEntity<>(tutorial, HttpStatus.OK);
    }

    @GetMapping("/tutorials/published")
    public ResponseEntity<List<Tutorial>> getAllPublishedTutorials() throws ResourceNotFoundException {
        List<Tutorial> tutorials = tutorialRepository.findByPublished(true);
        if(null == tutorials) {
            throw new ResourceNotFoundException("No published tutorials found");
        }
        return new ResponseEntity<>(tutorials, HttpStatus.OK);
    }
}
