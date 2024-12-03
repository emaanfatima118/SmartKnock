package com.example.smartknock;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class setdev extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private String userId;
    private DeviceController deviceController;

    private EditText deviceIdEditText;
    private EditText pinEditText;
    private Button connectButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setdev);

        // Initialize Firebase Auth and Device Controller
        mAuth = FirebaseAuth.getInstance();
        deviceController = new DeviceController();

        // Get the userId from FirebaseAuth
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            userId = user.getUid();
        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        // Initialize UI components
        deviceIdEditText = findViewById(R.id.box1); // Assuming EditText for device ID
        pinEditText = findViewById(R.id.box2); // Assuming EditText for PIN
        connectButton = findViewById(R.id.associateButton); // Assuming Button for connect action

        // Set click listener for the connect button
        connectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the device ID and PIN from the EditTexts
                String deviceId = deviceIdEditText.getText().toString();
                String pinString = pinEditText.getText().toString(); // Get PIN as string

                // Validate the inputs
                if (deviceId.isEmpty() || pinString.isEmpty()) {
                    Toast.makeText(setdev.this, "Please enter both device ID and PIN", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Parse PIN to integer with error handling
                Integer pin = null;
                try {
                    pin = Integer.parseInt(pinString); // Try parsing PIN as integer
                } catch (NumberFormatException e) {
                    Toast.makeText(setdev.this, "Invalid PIN entered", Toast.LENGTH_SHORT).show();
                    return; // Exit the method if PIN is not valid
                }

                // Set the status (assuming true for now)
                boolean status = true;

                // Call method to connect device to user
                connectDeviceToUser(deviceId, pin, status);
            }
        });
    }

    // Function to connect device to user
    private void connectDeviceToUser(String deviceId, int pin, boolean status) {
        if (userId == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(this, "Connecting Device. ID: " + deviceId + ", PIN: " + pin + ", Status: " + status, Toast.LENGTH_SHORT).show();
        // Log device details for debugging
        // Log.d("setdev", "Connecting device with ID: " + deviceId + " and PIN: " + pin);

        // Use DeviceController to connect the device
        deviceController.connectDevice(userId, deviceId, pin, status, new DatabaseCallback() {
            @Override
            public void onSuccess(String successMessage) {
                // Navigate to homepage after success
                Intent intent = new Intent(setdev.this, homepage.class);
                intent.putExtra("connectedDeviceId", deviceId); // Pass the connected device ID
                startActivity(intent);
                finish(); // Close the current activity
            }

            @Override
            public void onFailure(String error) {
                Toast.makeText(setdev.this, "Failed to connect device: " + error, Toast.LENGTH_LONG).show();
            }
        });
    }
}
