package com.example.smartknock;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.util.List;

public class visitorDetails extends AppCompatActivity {

    Button backToVisitorList;
    Button editVisitorName;
    ImageView visitorImage;
    TextView visitorName;
    TextView visitTime;
    ImageButton helpp, settings, feedback, logout, back;
    VisitorController visitorController; // Controller for fetching visitor data

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_visitor_details);

        // Initialize UI elements
        backToVisitorList = findViewById(R.id.back_to_visitor_list);
        editVisitorName = findViewById(R.id.edit_name_button);
        back = findViewById(R.id.homeButton);
        helpp = findViewById(R.id.helpcenterButton);
        settings = findViewById(R.id.settingsButton);
        feedback = findViewById(R.id.feedbackButton);
        logout = findViewById(R.id.logoutButton);
        visitorImage = findViewById(R.id.visitor_image);
        visitorName = findViewById(R.id.visitor_name);
        visitTime = findViewById(R.id.visit_time);

        // Initialize the VisitorController
        visitorController = new VisitorController();

        // Get the visitorId from the Intent
        Intent intent = getIntent();
        String visitorId = intent.getStringExtra("visitorId");
        if (visitorId != null) {
            fetchVisitorDetails(visitorId);
        } else {
            Toast.makeText(this, "Visitor ID not provided", Toast.LENGTH_SHORT).show();
        }

        // Edit visitor name
        editVisitorName.setOnClickListener(v -> {
            Intent intent1 = new Intent(visitorDetails.this, nameKnownVisitor.class);
            intent1.putExtra("visitorId", visitorId); // Pass the visitorId to the next activity
            startActivity(intent1);
            finish();
        });

        backToVisitorList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(visitorDetails.this, visitors.class);
                startActivity(intent);
                finish();
            }
        });

        helpp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(visitorDetails.this, helpcentre.class);
                startActivity(i);
                finish();
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(visitorDetails.this, homepage.class);
                startActivity(i);
                finish();
            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(visitorDetails.this, settings.class);
                startActivity(i);
                finish();
            }
        });
        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(visitorDetails.this, feedback.class);
                startActivity(i);
                finish();
            }
        });

    }

    private void fetchVisitorDetails(String visitorId) {
        visitorController.getVisitorById(visitorId, new VisitorCallback() {
            @Override
            public void onVisitorFetched(VisitorView visitor) {
                visitorName.setText(visitor.getName() != null ? visitor.getName() : "Unknown Visitor");
                visitTime.setText("Visit Time: " + visitor.getDatetime().toString());

                // Set visitor image
                if (visitor.getImageUrl() != null && !visitor.getImageUrl().isEmpty()) {
                    Glide.with(visitorDetails.this)
                            .load(visitor.getImageUrl())
                            .placeholder(R.drawable.ic_default_user)
                            .into(visitorImage);
                } else {
                    visitorImage.setImageResource(R.drawable.ic_default_user);
                }
            }

            @Override
            public void onVisitorsFetched(List<VisitorView> visitors) {
                // Not used here
            }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(visitorDetails.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
