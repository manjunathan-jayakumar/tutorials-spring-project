package com.hibernate_concepts.spring_jpa_mapping.controllers;

import com.hibernate_concepts.spring_jpa_mapping.exceptions.ResourceNotFoundException;
import com.hibernate_concepts.spring_jpa_mapping.models.Tag;
import com.hibernate_concepts.spring_jpa_mapping.models.Tutorial;
import com.hibernate_concepts.spring_jpa_mapping.repository.TagRepository;
import com.hibernate_concepts.spring_jpa_mapping.repository.TutorialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.ReadOnlyFileSystemException;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/api")
public class TagController {

    @Autowired
    private TutorialRepository tutorialRepository;

    @Autowired
    private TagRepository tagRepository;

    @PostMapping("/tutorials/{tutorialId}/tags")
    public ResponseEntity<Tag> addTag(@PathVariable(value = "tutorialId") Long tutorialId,
                                      @RequestBody Tag tagRequest) throws ResourceNotFoundException {
        Tag tag = tutorialRepository.findById(tutorialId).map(tutorial -> {
            String tagName = tagRequest.getName();
            if(tagRepository.findByName(tagName) != null) {
                Tag _tag = tagRepository.findByName(tagName);
                tutorial.addTag(_tag);
                tutorialRepository.save(tutorial);
                return _tag;
            }
            tutorial.addTag(tagRequest);
            return tagRepository.save(tagRequest);
        }).orElseThrow(() -> new ResourceNotFoundException("No tutorial Found with Id: "+ tutorialId));
        return new ResponseEntity<>(tag, HttpStatus.CREATED);
    }

    @GetMapping("/tags")
    public ResponseEntity<List<Tag>> getAllTags() {
        List<Tag> tags = new ArrayList<>();
        tagRepository.findAll().forEach(tags::add);
        if(tags.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(tags, HttpStatus.OK);
    }

    @GetMapping("/tags/{id}")
    public ResponseEntity<Tag> getTagsById(@PathVariable("id") Long id) throws ResourceNotFoundException {
        Tag _tag = tagRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No tag Found with id: "+id));
        return new ResponseEntity<>(_tag, HttpStatus.OK);
    }

    @GetMapping("tutorials/{tutorialId}/tags")
    public ResponseEntity<List<Tag>> getAllTagsByTutorialId(@PathVariable Long tutorialId)
            throws ResourceNotFoundException {
        if(!tutorialRepository.existsById(tutorialId)) {
            throw new ResourceNotFoundException("No Tutorial Found id: "+ tutorialId);
        }
        List<Tag> tags = tagRepository.findTagsByTutorialsId(tutorialId);
        return new ResponseEntity<>(tags, HttpStatus.OK);
    }

    @PutMapping("tags/{id}")
    public ResponseEntity<Tag> updateTag(@PathVariable("id") long id, @RequestBody Tag tagRequest) throws ResourceNotFoundException {
        Tag tag = tagRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Tag id: "+id+" not found"));

        tag.setName(tagRequest.getName());
        return new ResponseEntity<>(tagRepository.save(tag), HttpStatus.OK);
    }

    @DeleteMapping("/tutorials/{tutorialId}/tags/{tagId}")
    public ResponseEntity<?> deleteTagForATutorial(
            @PathVariable("tutorialId") Long tutorialId, @PathVariable("tagId") Long tagId) throws ResourceNotFoundException {
        Tutorial tutorial = tutorialRepository.findById(tutorialId)
                .orElseThrow(() -> new ResourceNotFoundException("Tutorial not found with id: "+tutorialId));
        tutorial.removeTag(tagId);
        tutorialRepository.save(tutorial);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

//    @DeleteMapping("/tags/{id}")
//    public ResponseEntity<?> deleteATag(@PathVariable Long id) {
//        List<Tutorial> tutorials =
//    }
}
