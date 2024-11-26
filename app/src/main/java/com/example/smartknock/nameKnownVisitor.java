package com.example.smartknock;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class nameKnownVisitor extends AppCompatActivity {

    Button backToVisitorDetails;
    Button saveName;
    ImageButton helpp, settings, feedback, logout, back;
    EditText visitorNameInput;

    VisitorController visitorController; // Controller for updating visitor name
    String visitorId; // Visitor ID passed via Intent

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_name_known_visitor);

        // Initialize UI elements
        backToVisitorDetails = findViewById(R.id.back_to_visitor_detail);
        saveName = findViewById(R.id.save_name_button);
        back = findViewById(R.id.homeButton);
        helpp = findViewById(R.id.helpcenterButton);
        settings = findViewById(R.id.settingsButton);
        feedback = findViewById(R.id.feedbackButton);
        logout = findViewById(R.id.logoutButton);
        visitorNameInput = findViewById(R.id.visitor_name_input); // Input for visitor's name

        // Initialize VisitorController
        visitorController = new VisitorController();

        // Get visitorId from Intent
        visitorId = getIntent().getStringExtra("visitorId");

        if (visitorId == null) {
            Toast.makeText(this, "Visitor ID not provided", Toast.LENGTH_SHORT).show();
            finish(); // Exit the activity if no visitorId is provided
        }

        // Set up button listeners
        backToVisitorDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(nameKnownVisitor.this, visitorDetails.class);
                startActivity(intent);
                finish();
            }
        });

        saveName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveVisitorName(); // Call the saveVisitorName method
            }
        });

        helpp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(nameKnownVisitor.this, helpcentre.class);
                startActivity(i);
                finish();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(nameKnownVisitor.this, homepage.class);
                startActivity(i);
                finish();
            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(nameKnownVisitor.this, settings.class);
                startActivity(i);
                finish();
            }
        });
        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(nameKnownVisitor.this, feedback.class);
                startActivity(i);
                finish();
            }
        });
    }

    /**
     * Save the visitor's name to Firebase.
     */
    private void saveVisitorName() {
        String visitorName = visitorNameInput.getText().toString().trim(); // Get user input

        if (visitorName.isEmpty()) {
            // Check for empty input
            Toast.makeText(this, "Please enter a name", Toast.LENGTH_SHORT).show();
            return; // Exit the method if input is invalid
        }

        // Use VisitorController to update the visitor's name
        visitorController.updateVisitorName(visitorId, visitorName, new VisitorCallback() {
            @Override
            public void onVisitorFetched(VisitorView visitor) {
                // Success callback
                Toast.makeText(nameKnownVisitor.this, "Name updated successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(nameKnownVisitor.this, visitors.class);
                startActivity(intent);
                finish(); // Return to visitors list
            }

            @Override
            public void onVisitorsFetched(List<VisitorView> visitors) {
                // Not used here
            }

            @Override
            public void onError(String errorMessage) {
                // Handle error
                Toast.makeText(nameKnownVisitor.this, "Error: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
