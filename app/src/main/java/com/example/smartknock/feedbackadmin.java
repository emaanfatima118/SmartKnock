package com.example.smartknock;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class feedbackadmin extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FeedbackAdapter feedbackAdapter;
    private List<FeedbackClass> feedbackList;          // Original list of all feedbacks
    private List<FeedbackClass> filteredFeedbackList;  // Filtered list for displaying
    private Spinner ratingSpinner;
    private ImageButton logout, backk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_feedbackadmin);

        // Initialize UI components
        recyclerView = findViewById(R.id.recyclerView);
        logout = findViewById(R.id.logoutButton);
        backk = findViewById(R.id.homeButton);
        ratingSpinner = findViewById(R.id.rating_spinner);

        // Initialize RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        feedbackList = new ArrayList<>();
        filteredFeedbackList = new ArrayList<>();
        feedbackAdapter = new FeedbackAdapter(this, filteredFeedbackList);
        recyclerView.setAdapter(feedbackAdapter);

        // Fetch feedback data
        fetchFeedbacks();

        // Set up Spinner for filtering
        setupSpinner();

        // Back button listener
        backk.setOnClickListener(v -> {
            Intent i = new Intent(feedbackadmin.this, admin_homepage.class);
            startActivity(i);
            finish();
        });

        // Logout button listener
        logout.setOnClickListener(v -> {
            getSharedPreferences("UserSession", MODE_PRIVATE)
                    .edit()
                    .clear() // Clear all session-related data
                    .apply(); // Apply changes

            // Show a toast message to confirm logout
            Toast.makeText(feedbackadmin.this, "Logged out successfully", Toast.LENGTH_SHORT).show();

            // Redirect user to the login screen
            Intent intent = new Intent(feedbackadmin.this, login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear activity stack
            startActivity(intent);

            // Finish the current activity
            finish();
        });
    }

    private void fetchFeedbacks() {
        FeedbackController feedbackController = new FeedbackController();
        feedbackController.fetchFeedbacks(new FeedbackAdminCallback() {
            @Override
            public void onSuccess(List<FeedbackClass> feedbacks) {
                feedbackList.clear();
                feedbackList.addAll(feedbacks);
                resetFeedbackList();
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(feedbackadmin.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupSpinner() {
        // Spinner values (All, 1-5 ratings)
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,
                new String[]{"All", "1", "2", "3", "4", "5"});
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ratingSpinner.setAdapter(adapter);

        // Listener for Spinner item selection
        ratingSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedRating = (String) parent.getItemAtPosition(position);
                if (selectedRating.equals("All")) {
                    resetFeedbackList(); // Show all feedbacks
                } else {
                    int rating = Integer.parseInt(selectedRating);
                    filterFeedbacksByRating(rating);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });
    }

    private void resetFeedbackList() {
        // Reset filtered list to show all feedbacks
        filteredFeedbackList.clear();
        filteredFeedbackList.addAll(feedbackList);
        feedbackAdapter.notifyDataSetChanged();
    }

    private void filterFeedbacksByRating(int rating) {
        filteredFeedbackList.clear(); // Clear the current filtered list
        for (FeedbackClass feedback : feedbackList) {
            if (feedback.getRating() == rating) {
                filteredFeedbackList.add(feedback); // Add matching feedbacks
            }
        }
        feedbackAdapter.notifyDataSetChanged(); // Notify adapter of changes
    }
}
