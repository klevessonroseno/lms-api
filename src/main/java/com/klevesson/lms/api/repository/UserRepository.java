package com.klevesson.lms.api.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.stereotype.Repository;

import com.klevesson.lms.api.config.ConnectionFactory;
import com.klevesson.lms.api.entity.User;

@Repository
public class UserRepository {
    
    private final ConnectionFactory connectionFactory;

    public UserRepository(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public User save(User user) {

        final String sql = """
            INSERT INTO users (
                name, 
                email, 
                username, 
                password_hash
            ) VALUES (?, ?, ?, ?)
            """;

        try (
            Connection connection = connectionFactory.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(
                sql, Statement.RETURN_GENERATED_KEYS
            );
        ) {

            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, user.getUsername());
            preparedStatement.setString(4, user.getPasswordHash());
            
            int affectedRows = preparedStatement.executeUpdate();

            if (affectedRows != 1) {
                throw new IllegalStateException(
                    "Expected exactly one affected row, but got " + affectedRows + "."
                );
            }

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
        
                if (generatedKeys.next()) {

                    Long id = generatedKeys.getLong(1);
                    user.setId(id);

                    return user;
                }

                throw new IllegalStateException(
                    "The insert operation succeeded, but no generated key was returned."
                );
            }

        } catch (SQLException error) {
            throw new RuntimeException(
                "An error occurred while saving.",
                error
            );
        }
    }
}
