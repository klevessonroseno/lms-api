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

    public Long save(Course course) {

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
            PreparedStatement statement = connection.prepareStatement(
                sql, Statement.RETURN_GENERATED_KEYS
            );
        ) {

            statement.setString(1, course.getSlug());
            statement.setString(2, course.getTitle());
            statement.setString(3, course.getDescription());
            statement.setLong(4, course.getLessons());
            statement.setLong(5, course.getHours());

            int affectedRows = statement.executeUpdate();

            if (affectedRows != 1) {
                throw new IllegalStateException(
                    "Expected exactly one affected row, but got " + affectedRows + "."
                );
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {

                if (generatedKeys.next()) {
                    Long id = generatedKeys.getLong(1);
                    return id;
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
