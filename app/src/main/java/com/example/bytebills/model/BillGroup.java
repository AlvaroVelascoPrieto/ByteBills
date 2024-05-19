package com.example.bytebills.model;
public class BillGroup {
    private int id;
    private String title;
    private String description;
    private String lastUpdateDate;

    private String currentPrice;

    private String sessionDelta;

    public BillGroup(int id, String title, String description, String lastUpdateDate, String currentPrice, String sessionDelta) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.lastUpdateDate = lastUpdateDate;
        this.currentPrice = currentPrice;
        this.sessionDelta = sessionDelta;
    }

    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(String lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }
    public String getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(String currentPrice) {
        this.currentPrice = currentPrice;
    }

    public String getSessionDelta() {
        return sessionDelta;
    }

    public void setSessionDelta(String sessionDelta) {
        this.sessionDelta = sessionDelta;
    }
}