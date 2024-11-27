package com.example.smartknock;/*package com.example.doorbell;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class setdev extends AppCompatActivity {

    EditText devicePinInput, devicePasswordInput;
    Button saveDeviceSetupButton;
    UserController userController;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setdev);

        // Initialize UI components
        devicePinInput = findViewById(R.id.box1);
        devicePasswordInput = findViewById(R.id.box2);
        saveDeviceSetupButton = findViewById(R.id.associateButton);

        // Initialize UserController
        userController = new UserController();

        // Get the userId from intent (passed after signup)
        userId = getIntent().getStringExtra("userId");

        // Save device setup
        saveDeviceSetupButton.setOnClickListener(view -> {
            String pin = devicePinInput.getText().toString().trim();
            String devicePassword = devicePasswordInput.getText().toString().trim();

            if (pin.isEmpty() || devicePassword.isEmpty()) {
                Toast.makeText(setdev.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            userController.updateDeviceSetup(userId, pin, devicePassword, new SignupCallback() {
                @Override
                public void onSignupResult(boolean isSuccess, String message) {
                    runOnUiThread(() -> {
                        Toast.makeText(setdev.this, message, Toast.LENGTH_SHORT).show();
                        if (isSuccess) {
                            finish(); // Close activity on success
                        }
                    });
                }
            });
        });
    }
}
*/