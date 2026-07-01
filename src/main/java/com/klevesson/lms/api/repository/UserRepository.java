package com.klevesson.lms.api.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

        String sql = "INSERT INTO users (name, email, username, password_hash) VALUES (?, ?, ?, ?)";

        try (
            Connection connection = connectionFactory.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ) {

            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, user.getUsername());
            preparedStatement.setString(4, user.getPasswordHash());
            
            preparedStatement.executeUpdate();

            try (
                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            ) {
                if (generatedKeys.next()) {
                    Long id = generatedKeys.getLong(1);

                    return id;
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Erro ao salvar.", e);
        }

        return null;
    }

}
