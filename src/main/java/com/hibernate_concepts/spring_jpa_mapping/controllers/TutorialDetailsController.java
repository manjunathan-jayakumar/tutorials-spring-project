package com.hibernate_concepts.spring_jpa_mapping.controllers;

import com.hibernate_concepts.spring_jpa_mapping.exceptions.ResourceNotFoundException;
import com.hibernate_concepts.spring_jpa_mapping.models.Tutorial;
import com.hibernate_concepts.spring_jpa_mapping.models.TutorialDetails;
import com.hibernate_concepts.spring_jpa_mapping.repository.TutorialDetailsRepository;
import com.hibernate_concepts.spring_jpa_mapping.repository.TutorialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@CrossOrigin("*")
@RestController
@RequestMapping("/api")
public class TutorialDetailsController {

    @Autowired
    private TutorialRepository tutorialRepository;

    @Autowired
    private TutorialDetailsRepository tutorialDetailsRepository;

    @PostMapping("/tutorials/{tutorialId}/details")
    public ResponseEntity<TutorialDetails> createDetails(@PathVariable("tutorialId")
                                                         Long tutorialId,
                                                         @RequestBody
                                                         TutorialDetails tutorialDetails)
            throws ResourceNotFoundException {
        Tutorial _tutorial = tutorialRepository.findById(tutorialId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("No tutorial found with id: "+
                                tutorialId)
                );
        tutorialDetails.setCreatedOn(LocalDateTime.now());
        tutorialDetails.setTutorial(_tutorial);
        TutorialDetails details = tutorialDetailsRepository.save(tutorialDetails);
        return new ResponseEntity<>(details, HttpStatus.CREATED);
    }

    @GetMapping({"/details/{id}", "/tutorials/{id}/details"})
    public ResponseEntity<TutorialDetails> getDetailsById(@PathVariable("id") Long id) throws ResourceNotFoundException {
        TutorialDetails details = tutorialDetailsRepository.findById(id)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Detail no found for tutorial id: " + id)
                );
        return new ResponseEntity<>(details, HttpStatus.OK);
    }

    @DeleteMapping({"/details/{id}", "/tutorials/{id}/details"})
    public ResponseEntity<Void> deleteTutorialDetailsById(@PathVariable Long id) throws ResourceNotFoundException {
        TutorialDetails tutorialDetails = tutorialDetailsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("TutorialDetails not found"));

        tutorialDetailsRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping({"/details/{id}", "/tutorials/{id}/details"})
    public ResponseEntity<TutorialDetails> updateTutorialDetails(@PathVariable long id,
                                                   @RequestBody TutorialDetails tutorialDetails)
            throws ResourceNotFoundException {
        TutorialDetails _tutorialDetails = tutorialDetailsRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Tutorial details with id: "+ id+ " not found")
        );
        _tutorialDetails.setCreatedBy(tutorialDetails.getCreatedBy());

        return new ResponseEntity<>(tutorialDetailsRepository.save(_tutorialDetails), HttpStatus.OK);
    }
}
