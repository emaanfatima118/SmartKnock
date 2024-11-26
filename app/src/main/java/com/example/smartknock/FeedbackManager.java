package com.example.smartknock;

import android.util.Log;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.CollectionReference;

import java.util.HashMap;
import java.util.Map;

public class FeedbackManager {

    private static FeedbackManager instance;
    private FirebaseFirestore db;

    private FeedbackManager() {
        db = FirebaseFirestore.getInstance();
    }

    // Singleton pattern
    public static synchronized FeedbackManager getInstance() {
        if (instance == null) {
            instance = new FeedbackManager();
        }
        return instance;
    }

    public void submitFeedback(String feedbackMessage, int rating, FirebaseUser currentUser, FeedbackCallback callback) {
        if (currentUser == null) {
            callback.onError("User is not logged in");
            return;
        }

        String userId = currentUser.getUid();
        String feedbackId = db.collection("feedbacks").document().getId(); // Firestore auto-generated ID

        // Create a map of feedback data
        Map<String, Object> feedbackData = new HashMap<>();
        feedbackData.put("userId", userId);
        feedbackData.put("message", feedbackMessage);
        feedbackData.put("rating", rating);
        feedbackData.put("timestamp", System.currentTimeMillis()); // Add a timestamp to track feedback submission

        // Save the feedback data to Firestore
        CollectionReference feedbackCollection = db.collection("feedbacks");
        DocumentReference feedbackRef = feedbackCollection.document(feedbackId);

        feedbackRef.set(feedbackData)
                .addOnSuccessListener(aVoid -> {
                    Log.d("FeedbackManager", "Feedback submitted successfully");
                    callback.onFeedbackSubmitted(feedbackId); // Call the callback on success
                })
                .addOnFailureListener(e -> {
                    Log.e("FeedbackManager", "Failed to submit feedback", e);
                    callback.onError("Failed to submit feedback: " + e.getMessage()); // Handle failure
                });
    }
}
