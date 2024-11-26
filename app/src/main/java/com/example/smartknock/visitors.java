package com.example.smartknock;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class visitors extends AppCompatActivity {


    ImageButton backButton;
    RecyclerView recyclerView;
    ArrayList<VisitorView> visitorsList; // List to hold visitor data
    VisitorAdapter adapter; // RecyclerView adapter
    VisitorController visitorController; // Controller for fetching data
    ImageButton helpp, settings, feedback, logout, back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_visitors);

        // Initialize UI components
        backButton = findViewById(R.id.homeButton);
        recyclerView = findViewById(R.id.recyclerView);
        back = findViewById(R.id.homeButton);
        helpp = findViewById(R.id.helpcenterButton);
        settings = findViewById(R.id.settingsButton);
        feedback = findViewById(R.id.feedbackButton);
        logout = findViewById(R.id.logoutButton);
        // Set up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        visitorsList = new ArrayList<>();
        adapter = new VisitorAdapter(this, visitorsList);
        recyclerView.setAdapter(adapter);

        // Initialize the VisitorController
        visitorController = new VisitorController();

        // Fetch visitor data
        fetchVisitorData();

        // Back button logic
        backButton.setOnClickListener(view -> {
            Intent intent = new Intent(visitors.this, homepage.class);
            startActivity(intent);
            finish();
        });
        helpp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(visitors.this, helpcentre.class);
                startActivity(i);
                finish();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(visitors.this, homepage.class);
                startActivity(i);
                finish();
            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(visitors.this, settings.class);
                startActivity(i);
                finish();
            }
        });
        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(visitors.this, feedback.class);
                startActivity(i);
                finish();
            }
        });
    }

    /**
     * Fetch visitor data using VisitorController and VisitorCallback.
     */
//    private void fetchVisitorData() {
//        visitorController.getAllVisitors(new VisitorCallback() {
//            @Override
//            public void onVisitorFetched(VisitorView visitor) {
//                // Not used in this context (fetching a single visitor is handled elsewhere)
//            }
//
//            @Override
//            public void onVisitorsFetched(List<VisitorView> visitors) {
//                // Handle successful data fetch
//                visitorsList.clear();
//                visitorsList.addAll(visitors); // Add fetched data to the list
//                adapter.notifyDataSetChanged(); // Refresh RecyclerView
//            }
//
//            @Override
//            public void onError(String errorMessage) {
//                // Handle errors (e.g., display a Toast)
//                Toast.makeText(visitors.this, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
//                Log.e("VisitorsActivity", "Error fetching visitors: " + errorMessage);
//            }
//        });
//    }
    private void fetchVisitorData() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("visitors")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        visitorsList.clear(); // Clear existing list

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String visitorName = document.getString("visitorName");
                            Timestamp timestamp = document.getTimestamp("visitorDateTime");

                            // Add null checks to ensure data exists
                            if (visitorName != null && timestamp != null) {
                                Date dateTime = timestamp.toDate();
                                VisitorView visitor = new VisitorView(document.getId(), false, dateTime, null);
                                visitor.setName(visitorName);
                                visitorsList.add(visitor);
                            } else {
                                Log.w("VisitorsActivity", "Visitor data is missing required fields");
                            }
                        }

                        adapter.notifyDataSetChanged(); // Refresh RecyclerView
                    } else {
                        String errorMessage = task.getException() != null ? task.getException().getMessage() : "Unknown error";
                        Toast.makeText(visitors.this, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
                        Log.e("VisitorsActivity", "Error fetching visitors: " + errorMessage);
                    }
                });
    }
}