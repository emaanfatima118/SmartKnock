package com.example.smartknock;

import static com.example.smartknock.R.*;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class management extends AppCompatActivity {
    ImageButton back;
    ImageButton settings,visitors,logout,feedback;

    private FirebaseFirestore firestore;
    private FirebaseAuth mAuth;
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
        // Initialize Firebase instances
        firestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        back=findViewById(R.id.homeButton);
        settings = findViewById(R.id.settingsButton);
        visitors = findViewById(R.id.visitorButton);
        logout = findViewById(R.id.logoutButton);
        feedback = findViewById(R.id.feedbackButton);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(management.this, homepage.class);
                startActivity(i);
                finish();
            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(management.this, settings.class);
                startActivity(i);
                finish();
            }
        });
        visitors.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(management.this, visitors.class);
                startActivity(i);
                finish();
            }
        });
        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(management.this, feedback.class);
                startActivity(i);
                finish();
            }
        });


        // "Connect to Device" button functionality
        Button connectToDeviceButton = findViewById(R.id.b1);
        connectToDeviceButton.setOnClickListener(v -> {
            // Navigate to DeviceConnectionActivity
            fetchUserTypeAndNavigate();
            //     Intent intent = new Intent(management.this, setdevprimary.class);
            //   startActivity(intent);
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
                Toast.makeText(management.this, "Logged out successfully", Toast.LENGTH_SHORT).show();

                // Redirect user to the login screen
                Intent intent = new Intent(management.this, login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear activity stack
                startActivity(intent);

                // Finish the current activity
                finish();
            }
        });
    }
    private void fetchUserTypeAndNavigate() {
        // Get the currently logged-in user
        String userId = mAuth.getCurrentUser().getUid();

        // Fetch the userType from Firestore
        firestore.collection("users")
                .document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String userType = documentSnapshot.getString("userType");
                        if (userType != null) {
                            if (userType.equalsIgnoreCase("PrimaryUser")) {
                                // Navigate to the Primary user activity
                                Intent intent = new Intent(management.this, setdevprimary.class);
                                startActivity(intent);
                                finish();
                            } else if (userType.equalsIgnoreCase("SecondaryUser")) {
                                // Navigate to the Secondary user activity
                                Intent intent = new Intent(management.this, setdev.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(management.this, "Unknown user type.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(management.this, "User type not found.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(management.this, "User not found in Firestore.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(management.this, "Error fetching user data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}