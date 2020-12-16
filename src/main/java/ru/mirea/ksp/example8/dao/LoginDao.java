package ru.mirea.ksp.example8.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginDao {

    private final Connection connection;

    public LoginDao(Connection connection) {
        this.connection = connection;
    }

    public LoginUser login(String login) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement("select id, display_name, pass_hash from game_users where login = ?")) {
            stmt.setString(1, login);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    int id = rs.getInt(1);
                    String displayName = rs.getString(2);
                    String passwordHash = rs.getString(3);
                    return new LoginUser(id, displayName, passwordHash);
                } else {
                    return null;
                }
            }
        }
    }
}
