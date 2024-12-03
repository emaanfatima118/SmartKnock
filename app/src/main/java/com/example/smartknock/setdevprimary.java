package com.example.smartknock;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class setdevprimary extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private String userId;
    private DeviceController deviceController;

    private EditText deviceIdEditText;
    private EditText pinEditText;
    private Button connectButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setdevprimary);

        // Initialize Firebase Auth and Device Controller
        mAuth = FirebaseAuth.getInstance();
        deviceController = new DeviceController();
        FirebaseApp.initializeApp(this);

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
                    EditText newPinInput = findViewById(R.id.box3); // EditText for new PIN
                    String newPinString = newPinInput.getText().toString(); // New PIN

                    // Validate the inputs
                    if (deviceId.isEmpty() || pinString.isEmpty()) {
                        Toast.makeText(setdevprimary.this, "Please enter both device ID and PIN", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Validate the PIN format
                    if (!pinString.matches("\\d{4}")) { // Check if the PIN is exactly 4 digits
                        Toast.makeText(setdevprimary.this, "PIN must be exactly 4 numeric digits", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    int pin = Integer.parseInt(pinString);
                    int newPin = -1;

                    try {
                        if (!newPinString.isEmpty()) {
                            if (!newPinString.matches("\\d{4}")) { // Validate new PIN if provided
                                Toast.makeText(setdevprimary.this, "New PIN must be exactly 4 numeric digits", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            newPin = Integer.parseInt(newPinString);
                        }
                    } catch (NumberFormatException e) {
                        Toast.makeText(setdevprimary.this, "Invalid PIN entered", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // Connect the device to the user
                    connectDeviceToUser(deviceId, pin, true);

                    // If newPin is provided, update the device PIN
                    if (newPin > 0) {
                        Device device = new Device(deviceId, pin); // Assuming you have a Device constructor that takes deviceId and pin
                        updateDevicePin(deviceId, newPin, pin, device);
                    }
                }

        });
    }

    // Function to connect device to user
    private void connectDeviceToUser(String deviceId, int pin, boolean status) {
        if (userId == null) {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        // Use DeviceController to connect the device
        deviceController.connectDevice(userId, deviceId, pin, status, new DatabaseCallback() {
            @Override
            public void onSuccess(String successMessage) {
                // Navigate to homepage after success
                Intent intent = new Intent(setdevprimary.this, homepage.class);
                intent.putExtra("connectedDeviceId", deviceId); // Pass the connected device ID
                startActivity(intent);
                finish(); // Close the current activity
            }

            @Override
            public void onFailure(String error) {
                Toast.makeText(setdevprimary.this, "Failed to connect device: " + error, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void updateDevicePin(String deviceId, int newPin, int pin, Device device) {
        if (deviceId.isEmpty() || newPin <= 0) {
            Toast.makeText(this, "Invalid device ID or PIN", Toast.LENGTH_SHORT).show();
            return;
        }

        // Assuming deviceController has a method to update the device PIN
        deviceController.updateDevicePin(deviceId, newPin, pin, device, new DatabaseCallback() {
            @Override
            public void onSuccess(String successMessage) {
                Toast.makeText(setdevprimary.this, successMessage, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(String error) {
                Toast.makeText(setdevprimary.this, "Failed to update PIN: " + error, Toast.LENGTH_LONG).show();
            }
        });
    }
}
