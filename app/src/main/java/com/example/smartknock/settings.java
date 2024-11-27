package com.example.smartknock;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class settings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_settings);
        Button btnNotificationSettings = findViewById(R.id.btn_notification_settings);
        Button btnUserManagementSettings = findViewById(R.id.btn_user_management_settings);
        Button btnDndMode = findViewById(R.id.btn_dnd_mode);
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
    }
}