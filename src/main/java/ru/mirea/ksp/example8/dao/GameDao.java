package ru.mirea.ksp.example8.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GameDao {

    private final Connection connection;

    public GameDao(Connection connection) {
        this.connection = connection;
    }

    public int getClicks(int userId) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement("select clicks from game_users where id = ?")) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                } else {
                    return 0;
                }
            }
        }
    }

    public void addClick(int userId) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement("update game_users set clicks = clicks + 1 where id = ?")) {
            stmt.setInt(1, userId);
            stmt.executeUpdate();
        }
    }

    public int getMaxClicks() throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement("select max(clicks) from game_users")) {
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                } else {
                    return 0;
                }
            }
        }
    }

    public List<User> getWinners(int maxClicks) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement("select id, display_name from game_users where clicks = ? order by display_name")) {
            stmt.setInt(1, maxClicks);
            try (ResultSet rs = stmt.executeQuery()) {
                List<User> winners = new ArrayList<>();
                while (rs.next()) {
                    int id = rs.getInt(1);
                    String displayName = rs.getString(2);
                    winners.add(new User(id, displayName));
                }
                return winners;
            }
        }
    }
}
