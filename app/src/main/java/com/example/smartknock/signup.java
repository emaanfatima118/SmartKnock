package com.example.smartknock;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class signup extends AppCompatActivity {

    Button loginButton, proceedButton;
    EditText fullName, email, password;
    AuthenticationController authController;
    CheckBox adminCheckbox, userCheckbox; // Admin and User checkboxes

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Initialize UI components
        fullName = findViewById(R.id.signupUsername);
        email = findViewById(R.id.signupEmail);
        password = findViewById(R.id.signupPassword);
        loginButton = findViewById(R.id.loginButton);
        proceedButton = findViewById(R.id.signupButton);
        //  adminCheckbox = findViewById(R.id.adminCheckBox);
        // userCheckbox = findViewById(R.id.userCheckBox);

        // Initialize AuthenticationController
        authController = new AuthenticationController();

        // Navigate to login screen
        loginButton.setOnClickListener(view -> {
            Intent i = new Intent(signup.this, login.class);
            startActivity(i);
            finish();
        });

        // Handle signup process
        proceedButton.setOnClickListener(view -> {
            String userName = fullName.getText().toString().trim();
            String userEmail = email.getText().toString().trim();
            String userPassword = password.getText().toString().trim();

            // Validate inputs
            if (!validateUsername(userName) || !validateEmail(userEmail) || !validatePassword(userPassword)) {
                return;
            }
/*
            // Ensure one checkbox is selected
            if (!adminCheckbox.isChecked() && !userCheckbox.isChecked()) {
                Toast.makeText(signup.this, "Please select either Admin or User", Toast.LENGTH_SHORT).show();
                return;
            }
*/
            // Determine role based on checkbox
            // boolean isAdmin = adminCheckbox.isChecked();
            //    boolean isAdmin=false;

            //   boolean isPrimaryUser = userCheckbox.isChecked();
            boolean isPrimaryUser=false;
            // Call signup method with appropriate role
            authController.signupUser(userName, userEmail, userPassword,  isPrimaryUser, new SignupCallback() {
                @Override
                public void onSignupResult(boolean isSuccess, String message) {
                    runOnUiThread(() -> {
                        Toast.makeText(signup.this, message, Toast.LENGTH_SHORT).show();
                        if (isSuccess) {
                            // Navigate to homepage after successful signup
                            Intent intent = new Intent(signup.this,homepage.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                }
            });
        });
    }

    // Validate username
    private boolean validateUsername(String username) {
        if (username.isEmpty()) {
            Toast.makeText(this, "Username is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (username.length() < 3) {
            Toast.makeText(this, "Username must be at least 3 characters long", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!username.matches("[a-zA-Z0-9 ]+")) {
            Toast.makeText(this, "Username can only contain letters, numbers, and spaces", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    // Validate email
    private boolean validateEmail(String email) {
        if (email.isEmpty()) {
            Toast.makeText(this, "Email is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    // Validate password
    private boolean validatePassword(String password) {
        if (password.isEmpty()) {
            Toast.makeText(this, "Password is required", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (password.length() < 4) {
            Toast.makeText(this, "Password must be at least 4 characters long", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!password.matches(".*[a-zA-Z].*") || !password.matches(".*[0-9].*")) {
            Toast.makeText(this, "Password must contain at least one letter and one number", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
