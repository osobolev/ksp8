package ru.mirea.ksp.example8;

public class SessionUser {

    public static final String CURRENT_USER = "currentUser";

    public final int id;
    public final String displayName;

    public SessionUser(int id, String displayName) {
        this.id = id;
        this.displayName = displayName;
    }
}
