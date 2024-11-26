package com.example.smartknock;

import com.google.firebase.auth.FirebaseUser;

public class FeedbackController {

    private final FeedbackManager feedbackManager;

    public FeedbackController() {
        feedbackManager = FeedbackManager.getInstance(); // Singleton pattern
    }

    public void submitFeedback(String feedbackMessage, int rating, FirebaseUser currentUser, FeedbackCallback callback) {
        // Pass the feedback data to the FeedbackManager
        feedbackManager.submitFeedback(feedbackMessage, rating, currentUser, callback);
    }
}
