package com.klevesson.lms.api.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.klevesson.lms.api.config.ConnectionFactory;
import com.klevesson.lms.api.entity.Course;

@Repository
public class CourseRepository implements CrudRepository<Course, Long> {
    
    private final ConnectionFactory connectionFactory;

    public CourseRepository(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }
    
    private Course create(Course course) {

        final String sql = """
            INSERT INTO courses (
                slug,
                title,
                description,
                lessons,
                hours,
                created_at,
                updated_at
            ) VALUES (?, ?, ?, ?, ?, ?, ?)
            """;
        
        try (
            Connection connection = connectionFactory.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(
                sql, Statement.RETURN_GENERATED_KEYS
            );
        ) {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String currentTimeStamp = formatter.format(Instant.now().atZone(ZoneOffset.UTC));

            preparedStatement.setString(1, course.getSlug());
            preparedStatement.setString(2, course.getTitle());
            preparedStatement.setString(3, course.getDescription());
            preparedStatement.setLong(4, course.getLessons());
            preparedStatement.setLong(5, course.getHours());
            preparedStatement.setString(6, currentTimeStamp);
            preparedStatement.setString(7, currentTimeStamp);

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

    private Course update(Course course) {
        

        return null;
    }

    @Override
    public Course save(Course entity) {
        // TODO Auto-generated method stub
        return null;
    } 

    @Override
    public Optional<Course> findById(Long id) {

        final String sql = """
            SELECT
                id,
                slug,
                title,
                description,
                lessons,
                hours,
                created_at,
                updated_at
            FROM courses
            WHERE id = ?;
            """;

        try (
            Connection connection = connectionFactory.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ) {

            preparedStatement.setLong(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {

                if (resultSet.next()) {

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    
                    String createdAt = resultSet.getString("created_at");
                    String updatedAt = resultSet.getString("updated_at");

                    Course course = new Course();

                    course.setId(resultSet.getLong("id"));
                    course.setSlug(resultSet.getString("slug"));
                    course.setTitle(resultSet.getString("title"));
                    course.setDescription(resultSet.getString("description"));
                    course.setLessons(resultSet.getLong("lessons"));
                    course.setHours(resultSet.getLong("hours"));
                    course.setCreatedAt(
                        LocalDateTime
                            .parse(createdAt, formatter)
                            .toInstant(ZoneOffset.UTC)
                    );
                    course.setUpdatedAt(
                        LocalDateTime
                            .parse(updatedAt, formatter)
                            .toInstant(ZoneOffset.UTC)
                    );

                    return Optional.of(course);
                }

                return Optional.empty();
            }
            
        } catch (Exception error) {
            throw new RuntimeException(
                "An error occurred.",
                error
            );
        }
    }
 
    @Override
    public List<Course> findAll() {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public void deleteById(Long id) {
        // TODO Auto-generated method stub
        
    }

}
