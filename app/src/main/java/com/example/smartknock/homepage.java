package com.example.smartknock;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.HashSet;
import java.util.Set;

public class homepage extends AppCompatActivity {
    ImageButton helpp, settings, visitors, logout, feedback;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firestore;
    private TextView connectedDevicesText; // To display connected devices
    private Button disconnectButton; // Disconnect button
    private DeviceController deviceController;
    @Override
    protected void onStart() {
        super.onStart();
        // Get the device ID passed from the setdev activity (if available)
        String connectedDeviceId = getIntent().getStringExtra("connectedDeviceId");
        if (connectedDeviceId != null) {
            // Refresh the list of connected devices
            fetchConnectedDevices();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        // Initialize Firebase Auth and Firestore
        mAuth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        FirebaseApp.initializeApp(this);  // Ensure Firebase is initialized before usage.
        deviceController = new DeviceController(); // Initialize DeviceController

        // UI elements
        connectedDevicesText = findViewById(R.id.connectedDeviceTextView); // TextView for devices
        disconnectButton = findViewById(R.id.disconnectButton);
        // Set up onClickListener for Disconnect button
        disconnectButton.setOnClickListener(v -> showDisconnectDialog());


        // Fetch connected devices
        fetchConnectedDevices();



        helpp =findViewById(R.id.helpcenterButton);

        settings =findViewById(R.id.settingsButton);

        visitors =findViewById(R.id.visitorButton);

     logout = findViewById(R.id.logoutButton);

        feedback =findViewById(R.id.feedbackButton);

        helpp.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick (View view){
                Intent i = new Intent(homepage.this, helpcentre.class);
                startActivity(i);
                finish();
            }
        });
        settings.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick (View view){
                Intent i = new Intent(homepage.this, settings.class);
                startActivity(i);
                finish();
            }
        });
        visitors.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick (View view){
                Intent i = new Intent(homepage.this, visitors.class);
                startActivity(i);
                finish();
            }
        });
        feedback.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick (View v){
                Intent i = new Intent(homepage.this, feedback.class);
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
                Toast.makeText(homepage.this, "Logged out successfully", Toast.LENGTH_SHORT).show();

                // Redirect user to the login screen
                Intent intent = new Intent(homepage.this, login.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear activity stack
                startActivity(intent);

                // Finish the current activity
                finish();
            }
        });
    }
    // Show dialog for disconnecting a device
    private void showDisconnectDialog() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            Toast.makeText(this, "User not logged in!", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = user.getUid();
        firestore.collection("UsersDevices")
                .whereEqualTo("userId", userId)
                // .whereEqualTo("status", "active")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult().size() > 0) {
                        String[] devices = new String[task.getResult().size()];
                        int index = 0;

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            devices[index++] = document.getString("deviceId");
                        }

                        // Create a dialog to display devices for disconnecting
                        AlertDialog.Builder builder = new AlertDialog.Builder(homepage.this);
                        builder.setTitle("Select Device to Disconnect")
                                .setItems(devices, (dialog, which) -> {
                                    String deviceToDisconnect = devices[which];
                                    disconnectDevice(deviceToDisconnect);
                                })
                                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                                .create()
                                .show();
                    } else {
                        Toast.makeText(this, "No active devices to disconnect!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Function to disconnect a device by calling the controller method
    private void disconnectDevice(String deviceId) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            Toast.makeText(this, "User not logged in!", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = user.getUid();

        // Call the controller to disconnect the device
        deviceController.disconnectDevice(userId, deviceId, new DatabaseCallback() {
            @Override
            public void onSuccess(String message) {
                Toast.makeText(homepage.this, message, Toast.LENGTH_SHORT).show();
                fetchConnectedDevices(); // Refresh the list of devices
            }

            @Override
            public void onFailure(String error) {
                Toast.makeText(homepage.this, error, Toast.LENGTH_SHORT).show();
            }
        });
    }
    // Fetch connected devices from Firestore
    private void fetchConnectedDevices() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            Toast.makeText(this, "User not logged in!", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = user.getUid();
        firestore.collection("UsersDevices")
                .whereEqualTo("userId", userId)
                //.whereEqualTo("status", "active") // Optional: Filter active devices only
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Set<String> uniqueDevices = new HashSet<>(); // To store unique device IDs
                        StringBuilder devices = new StringBuilder("Connected Devices:\n");

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String deviceId = document.getString("deviceId");

                            // Add only if the device is not already in the set
                            if (deviceId != null && uniqueDevices.add(deviceId)) {
                                devices.append(deviceId).append("\n");
                            }
                        }

                        // Display connected devices
                        if (uniqueDevices.isEmpty()) {
                            connectedDevicesText.setText("No connected devices.");
                        } else {
                            connectedDevicesText.setText(devices.toString());
                        }
                    } else {
                        Log.e("Firestore", "Error fetching devices", task.getException());
                        Toast.makeText(this, "Error fetching devices!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
