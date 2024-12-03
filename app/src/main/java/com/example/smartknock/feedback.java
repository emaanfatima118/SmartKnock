package com.example.smartknock;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class feedback extends AppCompatActivity {

    EditText feedbackMessage;
    RatingBar feedbackRating;
    ImageButton back;
    ImageButton helpp, settings, visitors, logout;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_feedback);

        // Initialize Firebase Authentication
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        // Initialize UI components
        feedbackMessage = findViewById(R.id.feedback_text);
        feedbackRating = findViewById(R.id.rating_bar);
        back = findViewById(R.id.homeButton);
        helpp = findViewById(R.id.helpcenterButton);
        settings = findViewById(R.id.settingsButton);
        visitors = findViewById(R.id.visitorButton);
        logout = findViewById(R.id.logoutButton);

        // Initialize the FeedbackController
        FeedbackController feedbackController = new FeedbackController();

        // Submit feedback logic
        findViewById(R.id.submit_button).setOnClickListener(view -> {
            String message = feedbackMessage.getText().toString().trim();
            int rating = (int) feedbackRating.getRating();

            // Submit feedback using FeedbackController
            feedbackController.submitFeedback(message, rating, currentUser, new FeedbackCallback() {
                @Override
                public void onFeedbackSubmitted(String feedbackId) {
                    // Feedback submission successful
                    Toast.makeText(feedback.this, "Feedback submitted successfully!", Toast.LENGTH_SHORT).show();
                    feedbackMessage.setText(""); // Clear message field
                    feedbackRating.setRating(0); // Reset rating bar
                    // Optionally, navigate to another page or show success
                }

                @Override
                public void onError(String errorMessage) {
                    // Handle feedback submission error
                    Toast.makeText(feedback.this, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
                }
            });
        });

        // Set up other button clicks like back, settings, etc.
        back.setOnClickListener(view -> {
            Intent intent = new Intent(feedback.this, homepage.class);
            startActivity(intent);
            finish();
        });
        helpp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(feedback.this, helpcentre.class);
                startActivity(i);
                finish();
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(feedback.this, settings.class);
                startActivity(i);
                finish();
            }
        });
        visitors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(feedback.this, visitors.class);
                startActivity(i);
                finish();
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Clear user session data from SharedPreferences (if used)
                getSharedPreferences("UserSession", MODE_PRIVATE)
                        .edit()
                        .clear() // Clear all session-related data
                        .apply(); // Apply changes

                // Show a toast message to confirm logout
                Toast.makeText(feedback.this, "Logged out successfully", Toast.LENGTH_SHORT).show();

                // Redirect user to the login screen
                Intent intent = new Intent(feedback.this, login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear activity stack
                startActivity(intent);

                // Finish the current activity
                finish();
            }
        });
        // Similarly, set up other buttons for navigation
    }
}
