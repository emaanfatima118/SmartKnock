package com.example.smartknock;

public interface FeedbackCallback {
    void onFeedbackSubmitted(String feedbackId);
    void onError(String errorMessage);
}
