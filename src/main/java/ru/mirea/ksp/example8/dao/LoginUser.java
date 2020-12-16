package ru.mirea.ksp.example8.dao;

public class LoginUser {

    public int id;
    public String displayName;
    public String passwordHash;

    public LoginUser(int id, String displayName, String passwordHash) {
        this.id = id;
        this.displayName = displayName;
        this.passwordHash = passwordHash;
    }
}
