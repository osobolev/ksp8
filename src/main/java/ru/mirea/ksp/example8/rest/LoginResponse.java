package ru.mirea.ksp.example8.rest;

public class LoginResponse {

    private boolean success;
    private String error;
    private String displayUser;

    public LoginResponse() {
    }

    public LoginResponse(boolean success, String error, String displayUser) {
        this.success = success;
        this.error = error;
        this.displayUser = displayUser;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getDisplayUser() {
        return displayUser;
    }

    public void setDisplayUser(String displayUser) {
        this.displayUser = displayUser;
    }
}
