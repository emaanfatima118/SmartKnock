package com.example.smartknock;

import com.google.firebase.Timestamp;

public class FeedbackClass {
    private String feedbackId;
    private String userId;
    private String message;
    private int rating;
    private String userName; // This will be set later

    public FeedbackClass() {
        // Default constructor (required by Firebase)
    }

    public FeedbackClass(String feedbackId, String userId, String message, int rating) {
        this.feedbackId = feedbackId;
        this.userId = userId;
        this.message = message;
        this.rating = rating;
    }

    // Getters and setters
    public String getFeedbackId() {
        return feedbackId;
    }

    public String getUserId() {
        return userId;
    }

    public String getMessage() {
        return message;
    }

    public int getRating() {
        return rating;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
