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

    public Long save(User user) {

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
            PreparedStatement statement = connection.prepareStatement(
                sql, Statement.RETURN_GENERATED_KEYS
            );
        ) {

            statement.setString(1, user.getName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getUsername());
            statement.setString(4, user.getPasswordHash());
            
            int affectedRows = statement.executeUpdate();

            if (affectedRows != 1) {
                throw new RuntimeException("Something went went wrong.");
            }

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                
                if (generatedKeys.next()) {
                    Long id = generatedKeys.getLong(1);
                    return id;
                }

                throw new RuntimeException("Something went wrong. No ID was generated.");
            }

        } catch (SQLException error) {
            throw new RuntimeException("Error saving.", error);
        }
    }

}
