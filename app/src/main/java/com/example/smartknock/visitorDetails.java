package com.example.smartknock;

import static android.text.format.DateUtils.formatDateTime;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.firestore.FirebaseFirestore;


public class visitorDetails extends AppCompatActivity {

    Button backToVisitorList;
    Button editVisitorName;
    ImageView visitorImage;
    TextView visitorName;
    TextView visitTime;

    ImageButton helpp, settings, feedback, logout, back;
    VisitorController visitorController;

    private ImageView imageView;
    private DatabaseReference databaseReference;

    FirebaseFirestore db;

    Bitmap bitmap;

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

        // Initialize VisitorController
        visitorController = new VisitorController();

        imageView = findViewById(R.id.visitor_image);

        db = FirebaseFirestore.getInstance();


        // Get visitorId from the Intent
        Intent intent = getIntent();
        String visitorId = intent.getStringExtra("visitorId");

        if (visitorId != null) {
            fetchVisitorDetails(visitorId);
        } else {
            Toast.makeText(this, "Visitor ID not provided", Toast.LENGTH_SHORT).show();
        }

        // Set up button listeners
        editVisitorName.setOnClickListener(v -> {
            Intent intent1 = new Intent(visitorDetails.this, nameKnownVisitor.class);
            intent1.putExtra("visitorId", visitorId); // Pass visitorId to next activity
            startActivity(intent1);
            finish();
        });

        backToVisitorList.setOnClickListener(v -> {
            Intent intent1 = new Intent(visitorDetails.this, visitors.class);
            startActivity(intent1);
            finish();
        });

        // Call setupNavigation to handle navigation buttons
        setupNavigation();
        //imagedisplay(base64);
    }

    private void fetchVisitorDetails(String visitorId) {
        db.collection("visitors")
                .document(visitorId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Fetch visitor name
                        String name = documentSnapshot.getString("visitorName");
                        visitorName.setText(name != null ? name : "Unknown Visitor");

                        // Fetch visit time (assuming stored as a Firestore Timestamp)
                        com.google.firebase.Timestamp timestamp = documentSnapshot.getTimestamp("visitorDateTime");
                        if (timestamp != null) {
                            // Convert Firestore Timestamp to Date and format it
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                            String formattedDate = sdf.format(timestamp.toDate());
                            visitTime.setText("Visit Time: " + formattedDate);
                        } else {
                            visitTime.setText("Visit Time: No Date");
                        }

                        // Fetch visitor image as a Base64 string
                        String visitorImageBase64 = documentSnapshot.getString("visitorImage");
                        if (visitorImageBase64 != null && !visitorImageBase64.isEmpty()) {
                            imagedisplay(visitorImageBase64);
                        } else {
                            visitorImage.setImageResource(R.drawable.ic_default_user);
                        }
                    } else {
                        Toast.makeText(visitorDetails.this, "Error: Visitor not found", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(visitorDetails.this, "Error fetching visitor details: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
//private void fetchVisitorDetails(String visitorId) {
//    FirebaseFirestore db = FirebaseFirestore.getInstance();
//
//    // Fetch visitor data from Firestore using visitorId
//    db.collection("visitors").document(visitorId).get()
//            .addOnSuccessListener(document -> {
//                if (document.exists()) {
//                    String name = document.getString("visitorName");
//                    String dateTime = document.getString("visitorDateTime");
//                    String imageBase64 = document.getString("visitorImage");
//
//                    // Set visitor name only if it's not already set
//                    if (name != null && !name.isEmpty()) {
//                        visitorName.setText(name);
//                    } else {
//                        visitorName.setText("Unknown Visitor");
//                    }
//
//                    // Format and display visit time
//                    visitTime.setText("Visit Time: " + (dateTime != null ? formatDateTime(dateTime) : "No Date"));
//
//                    // Decode and display image
//                    if (imageBase64 != null && !imageBase64.isEmpty()) {
//                        imagedisplay(imageBase64);
//                    } else {
//                        visitorImage.setImageResource(R.drawable.ic_default_user);
//                    }
//                } else {
//                    Toast.makeText(visitorDetails.this, "Visitor not found", Toast.LENGTH_SHORT).show();
//                }
//            })
//            .addOnFailureListener(e -> {
//                Toast.makeText(visitorDetails.this, "Error fetching visitor: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//            });
//}
    private String formatDateTime(String dateTime) {
        try {
            // Parse the original date string into a Date object
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            Date date = inputFormat.parse(dateTime);

            // Format the Date object into a more readable format
            SimpleDateFormat outputFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss", Locale.getDefault());
            return outputFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return "Invalid Date Format"; // In case of an error, return a fallback message
        }
    }

    private void imagedisplay(String base64){
        byte[] decodedString = Base64.decode(base64, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        visitorImage.setImageBitmap(decodedByte);
    }

    private void setupNavigation() {
        // Handle Help Center navigation
        helpp.setOnClickListener(view -> {
            Intent intent = new Intent(visitorDetails.this, helpcentre.class);
            startActivity(intent);
            finish();
        });

        // Handle Settings navigation
        settings.setOnClickListener(view -> {
            Intent intent = new Intent(visitorDetails.this, settings.class);
            startActivity(intent);
            finish();
        });

        // Handle Feedback navigation
        feedback.setOnClickListener(view -> {
            Intent intent = new Intent(visitorDetails.this, feedback.class);
            startActivity(intent);
            finish();
        });

        // Handle Logout
        logout.setOnClickListener(view -> {
            getSharedPreferences("UserSession", MODE_PRIVATE)
                    .edit()
                    .clear()
                    .apply();

            Toast.makeText(visitorDetails.this, "Logged out successfully", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(visitorDetails.this, login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }


}