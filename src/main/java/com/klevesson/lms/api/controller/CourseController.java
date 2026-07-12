package com.klevesson.lms.api.controller;

import com.klevesson.lms.api.repository.UserRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.klevesson.lms.api.entity.Course;
import com.klevesson.lms.api.repository.CourseRepository;

import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;



@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseRepository courseRepository;

    public CourseController(CourseRepository courseRepository, UserRepository userRepository) {
        this.courseRepository = courseRepository;
    }

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody Course course) {
        Course createdCourse = courseRepository.save(course);

        URI uri = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(createdCourse.getId())
                    .toUri();
        
        return ResponseEntity.created(uri).build();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Course> getOne(@PathVariable Long id) {
        Course course = courseRepository.findById(id).get();
        return ResponseEntity.ok().body(course);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(
        @PathVariable Long id,
        @RequestBody Course course
    ) {
        course.setId(id);
        courseRepository.save(course);
        
        return ResponseEntity.noContent().build();
    }

}
