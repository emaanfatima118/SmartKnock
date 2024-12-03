package com.example.smartknock;

import android.util.Log;

import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class FeedbackManager {

    private static FeedbackManager instance;
    private final FirebaseFirestore db;

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

    // Submit feedback method
    public void submitFeedback(String feedbackMessage, int rating, FirebaseUser currentUser, FeedbackCallback callback) {
        if (currentUser == null) {
            callback.onError("User is not logged in");
            return;
        }

        String userId = currentUser.getUid();
        String feedbackId = db.collection("feedbacks").document().getId(); // Firestore auto-generated ID

        // Feedback data
        FeedbackClass feedback = new FeedbackClass(feedbackId, userId, feedbackMessage, rating);

        // Save to Firestore
        db.collection("feedbacks").document(feedbackId)
                .set(feedback)
                .addOnSuccessListener(aVoid -> {
                    Log.d("FeedbackManager", "Feedback submitted successfully");
                    callback.onFeedbackSubmitted(feedbackId);
                })
                .addOnFailureListener(e -> {
                    Log.e("FeedbackManager", "Failed to submit feedback", e);
                    callback.onError("Failed to submit feedback: " + e.getMessage());
                });
    }

    // Fetch feedbacks with user names
    public void fetchFeedbackWithUsernames(FeedbackAdminCallback callback) {
        db.collection("feedbacks")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        List<FeedbackClass> feedbackList = new ArrayList<>();

                        // Iterate through documents
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String userId = document.getString("userId");
                            String message = document.getString("message");
                            Long ratingValue = document.getLong("rating"); // Store in a variable
                            int rating = (ratingValue != null) ? ratingValue.intValue() : 0; // Safely call intValue()

                            Timestamp timestamp = document.getTimestamp("timestamp");
                            String feedbackId = document.getId();

                            // Fetch user name
                            fetchUserName(userId, feedbackId, message, rating, timestamp, feedbackList, task.getResult().size(), callback);
                        }
                    } else {
                        Log.e("FeedbackManager", "Failed to fetch feedbacks", task.getException());
                        callback.onFailure(task.getException());
                    }
                });
    }

    private void fetchUserName(String userId, String feedbackId, String message, int rating, Timestamp timestamp,
                               List<FeedbackClass> feedbackList, int totalFeedbacks, FeedbackAdminCallback callback) {
        db.collection("users").document(userId)
                .get()
                .addOnSuccessListener(userDoc -> {
                    String userName = userDoc.exists() && userDoc.getString("name") != null ? userDoc.getString("name") : "Unknown User";

                    // Create feedback object
                    FeedbackClass feedback = new FeedbackClass(feedbackId, userId, message, rating);
                    feedback.setUserName(userName);

                    // Add to the list
                    feedbackList.add(feedback);

                    // Check if all feedbacks are processed
                    if (feedbackList.size() == totalFeedbacks) {
                        callback.onSuccess(feedbackList);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("FeedbackManager", "Failed to fetch user details for userId: " + userId, e);
                    callback.onFailure(e);
                });
    }


}
