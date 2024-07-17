package com.example.apptracnghiem.model;

import java.sql.Timestamp;

public class Test {

    private String username;
    private int category_id;
    private int score;
    private Timestamp timestamp;

    public Test() {
    }

    public Test(String username, int category_id, int score, Timestamp timestamp) {
        this.username = username;
        this.category_id = category_id;
        this.score = score;
        this.timestamp = timestamp;
    }

    public Test(int category_id, int score, Timestamp timestamp) {
        this.category_id = category_id;
        this.score = score;
        this.timestamp = timestamp;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Test{" +
                "username='" + username + '\'' +
                ", category_id=" + category_id +
                ", score=" + score +
                ", timestamp=" + timestamp +
                '}';
    }
}
