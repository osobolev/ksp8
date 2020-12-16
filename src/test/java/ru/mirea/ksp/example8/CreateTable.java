package ru.mirea.ksp.example8;

import at.favre.lib.crypto.bcrypt.BCrypt;
import at.favre.lib.crypto.bcrypt.LongPasswordStrategies;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CreateTable {

    private static final BCrypt.Hasher hasher = BCrypt.with(LongPasswordStrategies.truncate(BCrypt.Version.VERSION_2A));

    private static String hash(String password) {
        return hasher.hashToString(12, password.toCharArray());
    }

    public static void main(String[] args) throws SQLException {
        try (Connection connection = DriverManager.getConnection("jdbc:h2:~/game_db")) {
            try (PreparedStatement stmt = connection.prepareStatement(
                "create table game_users (" +
                "  id int primary key, " +
                "  login varchar(50) not null," +
                "  pass_hash varchar(60) not null," +
                "  display_name varchar(100) not null," +
                "  clicks int not null default 0" +
                ")"
            )) {
                stmt.execute();
            }
            try (PreparedStatement stmt = connection.prepareStatement(
                "insert into game_users" +
                "(id, login, pass_hash, display_name)" +
                "values" +
                "(?, ?, ?, ?)"
            )) {
                {
                    stmt.setInt(1, 100);
                    stmt.setString(2, "oleg");
                    stmt.setString(3, hash("123"));
                    stmt.setString(4, "Олег");
                    stmt.executeUpdate();
                }
                {
                    stmt.setInt(1, 200);
                    stmt.setString(2, "ivan");
                    stmt.setString(3, hash("456"));
                    stmt.setString(4, "Иван");
                    stmt.executeUpdate();
                }
            }
            connection.commit();
        }
    }
}
