package com.klevesson.lms.api.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ConnectionFactory {

    private final String url;

    public ConnectionFactory(@Value("${url}") String url) {
        this.url = url;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url);
    }
}