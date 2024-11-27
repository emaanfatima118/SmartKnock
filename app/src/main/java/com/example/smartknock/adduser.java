package com.example.smartknock;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class adduser extends AppCompatActivity {

    private EditText userNameInput;
    private EditText userEmailInput;
    private EditText userPasswordInput;
    private EditText deviceIdInput;
    private Button saveUserButton;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adduser);

        userNameInput = findViewById(R.id.signupUsername);
        userEmailInput = findViewById(R.id.signupEmail);
        userPasswordInput = findViewById(R.id.signupPassword);
        deviceIdInput = findViewById(R.id.box1);
        saveUserButton = findViewById(R.id.signupButton);

        // Initialize DatabaseHelper
        dbHelper = new DatabaseHelper();

        ImageView backButton = findViewById(R.id.btn2);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate back
                Intent i= new Intent(adduser.this, admin_homepage.class);
                startActivity(i);
            }
        });
        saveUserButton.setOnClickListener(v -> {
            String name = userNameInput.getText().toString().trim();
            String email = userEmailInput.getText().toString().trim();
            String password = userPasswordInput.getText().toString().trim();
            String deviceId = deviceIdInput.getText().toString().trim();

            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) ||
                    TextUtils.isEmpty(password) || TextUtils.isEmpty(deviceId)) {
                Toast.makeText(adduser.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            // Create a user object to add to Firestore
            Map<String, Object> user = new HashMap<>();
            user.put("name", name);
            user.put("email", email);
            user.put("password", password);
            user.put("device_id", deviceId);
            user.put("user_type", "Secondary User"); // Set user type to Secondary User

            // Add user via DatabaseHelper
            dbHelper.addUser(user, new DatabaseHelper.DataStatus() {
                @Override
                public void DataIsLoaded(List<User> users, String message) {
                    // Optionally handle this case if needed
                }

                @Override
                public void onFailure(String message) {
                    Toast.makeText(adduser.this, message, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onSuccess(String message) {
                    Toast.makeText(adduser.this, message, Toast.LENGTH_SHORT).show();


                        // Navigate back
                        Intent i= new Intent(adduser.this,admin_homepage.class);
                        startActivity(i);
                    
                }
            });

        });
    }
}
