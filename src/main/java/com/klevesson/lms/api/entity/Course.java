package com.klevesson.lms.api.entity;

import java.time.Instant;

public class Course {
    
    private Long id;

    private String slug;

    private String title;

    private String description;

    private Long lessons;

    private Long hours;

    private Instant createdAt;

    private Instant updatedAt;
    
    public Course(
        Long id, 
        String slug, 
        String title, 
        String description, 
        Long lessons, 
        Long hours,
        Instant createdAt, 
        Instant updatedAt
    ) {
        this.id = id;
        this.slug = slug;
        this.title = title;
        this.description = description;
        this.lessons = lessons;
        this.hours = hours;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public Course() {};

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getLessons() {
        return lessons;
    }

    public void setLessons(Long lessons) {
        this.lessons = lessons;
    }

    public Long getHours() {
        return hours;
    }

    public void setHours(Long hours) {
        this.hours = hours;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
