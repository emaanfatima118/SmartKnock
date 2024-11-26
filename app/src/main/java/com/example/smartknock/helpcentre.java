package com.example.smartknock;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class helpcentre extends AppCompatActivity {

    ImageButton back;
    ImageButton settings,visitors,logout,feedback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_helpcentre);
        back=findViewById(R.id.homeButton);
        settings = findViewById(R.id.settingsButton);
        visitors = findViewById(R.id.visitorButton);
        logout = findViewById(R.id.logoutButton);
        feedback = findViewById(R.id.feedbackButton);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(helpcentre.this, homepage.class);
                startActivity(i);
                finish();
            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(helpcentre.this, settings.class);
                startActivity(i);
                finish();
            }
        });
        visitors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(helpcentre.this, visitors.class);
                startActivity(i);
                finish();
            }
        });
        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(helpcentre.this, feedback.class);
                startActivity(i);
                finish();
            }
        });

    }
}