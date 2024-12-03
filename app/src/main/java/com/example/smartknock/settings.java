package com.example.smartknock;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class settings extends AppCompatActivity {
    ImageButton helpp, feedback, visitors, logout;
    ImageButton back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_settings);
        Button btnNotificationSettings = findViewById(R.id.btn_notification_settings);
        Button btnUserManagementSettings = findViewById(R.id.btn_user_management_settings);
        Button btnDndMode = findViewById(R.id.btn_dnd_mode);
        back = findViewById(R.id.homeButton);
        helpp = findViewById(R.id.helpcenterButton);
        feedback = findViewById(R.id.feedbackButton);
        visitors = findViewById(R.id.visitorButton);
        logout = findViewById(R.id.logoutButton);
        // Set OnClickListeners for each button
        btnNotificationSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to Notification Settings Activity
                startActivity(new Intent(settings.this, notify.class));
            }
        });

        btnUserManagementSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to User Management Settings Activity
                startActivity(new Intent(settings.this, management.class));
            }
        });

        btnDndMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to DND Settings Activity
                startActivity(new Intent(settings.this, dnd.class));
            }
        });
        // Set up other button clicks like back, settings, etc.
        back.setOnClickListener(view -> {
            Intent intent = new Intent(settings.this, homepage.class);
            startActivity(intent);
            finish();
        });
        helpp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(settings.this, helpcentre.class);
                startActivity(i);
                finish();
            }
        });

        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(settings.this, feedback.class);
                startActivity(i);
                finish();
            }
        });
        visitors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(settings.this, visitors.class);
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
                Toast.makeText(settings.this, "Logged out successfully", Toast.LENGTH_SHORT).show();

                // Redirect user to the login screen
                Intent intent = new Intent(settings.this, login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear activity stack
                startActivity(intent);

                // Finish the current activity
                finish();
            }
        });
    }
}