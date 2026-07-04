package com.klevesson.lms.api.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import org.springframework.stereotype.Repository;

import com.klevesson.lms.api.config.ConnectionFactory;
import com.klevesson.lms.api.entity.Course;

@Repository
public class CourseRepository {
    
    private final ConnectionFactory connectionFactory;

    public CourseRepository(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public Course save(Course course) {

        final String sql = """
            INSERT INTO courses (
                slug,
                title,
                description,
                lessons,
                hours
            ) VALUES (?, ?, ?, ?, ?)
            """;
        
        try (
            Connection connection = connectionFactory.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(
                sql, Statement.RETURN_GENERATED_KEYS
            );
        ) {

            preparedStatement.setString(1, course.getSlug());
            preparedStatement.setString(2, course.getTitle());
            preparedStatement.setString(3, course.getDescription());
            preparedStatement.setLong(4, course.getLessons());
            preparedStatement.setLong(5, course.getHours());

            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows != 1) {
                throw new IllegalStateException(
                    "Expected exactly one affected row, but got " + affectedRows + "."
                );
            }

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {

                if (generatedKeys.next()) {

                    Long id = generatedKeys.getLong(1);
                    course.setId(id);
                    
                    return course;
                }

                throw new IllegalStateException(
                    "The insert operation succeeded, but no generated key was returned."
                );
            }

        } catch (Exception error) {
            throw new RuntimeException(
                "An error occurred while saving.",
                error
            );
        }        
    }
}
