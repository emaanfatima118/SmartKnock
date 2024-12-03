package com.example.smartknock;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class login extends AppCompatActivity {

    EditText email, password;
    Button loginButton, signupButton;
    CheckBox adminCheckBox, userCheckBox;
    AuthenticationController authController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize UI components
        email = findViewById(R.id.loginUsername);
        password = findViewById(R.id.loginPassword);
        loginButton = findViewById(R.id.loginButton);
        signupButton = findViewById(R.id.signupButton);
        adminCheckBox = findViewById(R.id.adminCheckBox);
        userCheckBox = findViewById(R.id.userCheckBox);

        authController = new AuthenticationController();

        // Navigate to signup screen
        signupButton.setOnClickListener(view -> {
            Intent i = new Intent(login.this, signup.class);
            startActivity(i);
        });

        // Handle login process
        loginButton.setOnClickListener(view -> {
            String userEmail = email.getText().toString().trim();
            String userPassword = password.getText().toString().trim();

            // Validate fields
            if (userEmail.isEmpty() || userPassword.isEmpty()) {
                Toast.makeText(login.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Validate checkbox selection
            if (!adminCheckBox.isChecked() && !userCheckBox.isChecked()) {
                Toast.makeText(login.this, "Please select either Admin or User", Toast.LENGTH_SHORT).show();
                return;
            }

            if (adminCheckBox.isChecked()) {
                // Admin login
                authController.loginAdmin(userEmail, userPassword, new LoginCallback() {
                    @Override
                    public void onLoginResult(boolean isSuccess, String message) {
                        runOnUiThread(() -> {
                            Toast.makeText(login.this, message, Toast.LENGTH_SHORT).show();
                            if (isSuccess) {
                                Intent adminIntent = new Intent(login.this, admin_homepage.class);
                                startActivity(adminIntent);
                                finish();
                            }
                        });
                    }
                });
            } else if (userCheckBox.isChecked()) {
                // User login
                authController.loginUser(userEmail, userPassword, new LoginCallback() {
                    @Override
                    public void onLoginResult(boolean isSuccess, String message) {
                        runOnUiThread(() -> {
                            Toast.makeText(login.this, message, Toast.LENGTH_SHORT).show();
                            if (isSuccess) {
                                Intent userIntent = new Intent(login.this, homepage.class);
                                startActivity(userIntent);
                                finish();
                            }
                        });
                    }
                });
            }
        });
    }
}
/*package com.example.doorbell;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class login extends AppCompatActivity {

    EditText email, password;
    Button loginButton, signupButton;
    CheckBox adminCheckBox, userCheckBox;
    AuthenticationController authController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize UI components
        email = findViewById(R.id.loginUsername);
        password = findViewById(R.id.loginPassword);
        loginButton = findViewById(R.id.loginButton);
        signupButton = findViewById(R.id.signupButton);
        adminCheckBox = findViewById(R.id.adminCheckBox);
        userCheckBox = findViewById(R.id.userCheckBox);

        authController = new AuthenticationController();

        // Navigate to signup screen
        signupButton.setOnClickListener(view -> {
            Intent i = new Intent(login.this, signup.class);
            startActivity(i);
        });

        // Handle login process
        loginButton.setOnClickListener(view -> {
            String userEmail = email.getText().toString();
            String userPassword = password.getText().toString();

            // Validate fields
            if (userEmail.isEmpty() || userPassword.isEmpty()) {
                Toast.makeText(login.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Call loginUser with the callback
            authController.loginUser(userEmail, userPassword, new LoginCallback() {
                @Override
                public void onLoginResult(boolean isSuccess, String message) {
                    runOnUiThread(() -> {
                        Toast.makeText(login.this, message, Toast.LENGTH_SHORT).show();
                        if (isSuccess) {

                            // Save email and password to SharedPreferences
                            SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("primaryUserEmail", userEmail);
                            editor.putString("primaryUserPassword", userPassword);
                            editor.apply();
                            // Check the state of the admin checkbox
                            if (adminCheckBox.isChecked()) {
                                // Navigate to admin activity
                                Intent adminIntent = new Intent(login.this, admin_homepage.class);
                                startActivity(adminIntent);
                            } else {
                                // Navigate to user activity
                                Intent userIntent = new Intent(login.this, homepage.class);
                                startActivity(userIntent);
                            }
                            finish();
                        }
                    });
                }
            });




        });

    }
}
*/