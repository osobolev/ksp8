package ru.mirea.ksp.example8.rest;

import java.util.List;

public class GameStateResponse {

    private String currentUser;
    private boolean winner;
    private int clicks;
    private int maxClicks;
    private List<String> winners;

    public GameStateResponse() {
    }

    public GameStateResponse(String currentUser, boolean winner, int clicks, int maxClicks, List<String> winners) {
        this.currentUser = currentUser;
        this.winner = winner;
        this.clicks = clicks;
        this.maxClicks = maxClicks;
        this.winners = winners;
    }

    public String getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(String currentUser) {
        this.currentUser = currentUser;
    }

    public boolean isWinner() {
        return winner;
    }

    public void setWinner(boolean winner) {
        this.winner = winner;
    }

    public int getClicks() {
        return clicks;
    }

    public void setClicks(int clicks) {
        this.clicks = clicks;
    }

    public int getMaxClicks() {
        return maxClicks;
    }

    public void setMaxClicks(int maxClicks) {
        this.maxClicks = maxClicks;
    }

    public List<String> getWinners() {
        return winners;
    }

    public void setWinners(List<String> winners) {
        this.winners = winners;
    }
}
