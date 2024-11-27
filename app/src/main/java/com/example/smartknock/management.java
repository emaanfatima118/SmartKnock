package com.example.smartknock;

import static com.example.smartknock.R.*;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class management extends AppCompatActivity {

    private PrimaryUserBehavior primaryUserBehavior;
    private SecondaryUserBehavior secondaryUserBehavior;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_usermanagement);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.usermanagement), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Retrieve email and password from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String primaryUserEmail = sharedPreferences.getString("primaryUserEmail", "");
        String primaryUserPassword = sharedPreferences.getString("primaryUserPassword", "");

        if (!primaryUserEmail.isEmpty() && !primaryUserPassword.isEmpty()) {
            // Initialize behaviors with email and password
            primaryUserBehavior = new PrimaryUserBehavior(primaryUserEmail, primaryUserPassword);
            secondaryUserBehavior = new SecondaryUserBehavior();
        }

        // Back Button functionality
    ImageView backButton = findViewById(R.id.btn2);
        backButton.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            // Navigate back
            Intent i= new Intent(management.this,settings.class);
            startActivity(i);
        }
    });
        // Get the EditText view for the email
        EditText emailEditText = findViewById(id.emailEditText);  // Replace R.id.emailEditText with your actual EditText ID

        // Handle "Send Email Invitation" button click
        Button emailButton = findViewById(R.id.emailInviteButton);
        emailButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();  // Get email from EditText
            if (!email.isEmpty()) {
                primaryUserBehavior.sendEmailInvitation(email);
            } else {
                // Handle empty email input
                Toast.makeText(management.this, "Please enter a valid email", Toast.LENGTH_SHORT).show();
            }
        });
    }
}