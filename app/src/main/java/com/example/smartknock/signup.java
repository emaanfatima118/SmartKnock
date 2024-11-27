package com.example.smartknock;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class signup extends AppCompatActivity {

    Button loginButton, proceedButton;
    EditText fullName, email, password, deviceIdInput, pinInput;
    UserController userController;
    CheckBox adminCheckbox; // Admin checkbox

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Initialize UI components
        fullName = findViewById(R.id.signupUsername);
        email = findViewById(R.id.signupEmail);
        password = findViewById(R.id.signupPassword);
        deviceIdInput = findViewById(R.id.box1); // Device ID input
        pinInput = findViewById(R.id.box2); // PIN input (optional for secondary users)
        loginButton = findViewById(R.id.loginButton);
        proceedButton = findViewById(R.id.signupButton);
        adminCheckbox = findViewById(R.id.adminCheckBox); // Admin checkbox

        // Initialize UserController
        userController = new UserController();

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
            String deviceId = deviceIdInput.getText().toString().trim();
            String pin = pinInput.getText().toString().trim();

            // Check if admin checkbox is selected
            boolean isAdmin = adminCheckbox.isChecked();

            if (isAdmin) {
                // If admin, only validate name, email, and password
                if (userName.isEmpty() || userEmail.isEmpty() || userPassword.isEmpty()) {
                    Toast.makeText(signup.this, "Please fill in all required fields", Toast.LENGTH_SHORT).show();
                    return;
                }
            } else {
                // For non-admin users, validate all fields including deviceId and pin
                if (userName.isEmpty() || userEmail.isEmpty() || userPassword.isEmpty() || deviceId.isEmpty()) {
                    Toast.makeText(signup.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            // Determine if it's a primary user (non-admin with PIN)
            boolean isPrimaryUser = !isAdmin && !pin.isEmpty();

            // Call signupUser method
            userController.signupUser(userName, userEmail, userPassword, isAdmin, deviceId, pin, isPrimaryUser, new SignupCallback() {
                @Override
                public void onSignupResult(boolean isSuccess, String message) {
                    runOnUiThread(() -> {
                        Toast.makeText(signup.this, message, Toast.LENGTH_SHORT).show();
                        if (isSuccess) {
                            Intent intent = new Intent(signup.this, homepage.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                }
            });
        });
    }
}

//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.Toast;
//
//import androidx.activity.EdgeToEdge;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.graphics.Insets;
//import androidx.core.view.ViewCompat;
//import androidx.core.view.WindowInsetsCompat;
//
//import com.google.firebase.FirebaseApp;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//
//import java.util.UUID;
//
//public class signup extends AppCompatActivity {
//    Button btn;
//    Button proceedbtn;
//    String roll,name,password,email;
//    EditText Name,Email,Password;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        if (FirebaseApp.getApps(this).isEmpty()) {
//            FirebaseApp.initializeApp(this);
//        }
//
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_signup);
//        Email = findViewById(R.id.signupEmail);
//        Password = findViewById(R.id.signupPassword);
//        Name = findViewById(R.id.signupUsername);
//
//        btn=findViewById(R.id.loginButton);
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i= new Intent(signup.this, login.class);
//                Log.d("Firebase", "Data written successfully");
//                startActivity(i);
//                finish();
//            }
//        });
//        proceedbtn=findViewById(R.id.signupButton);
//        proceedbtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                name = Name.getText().toString().trim();
//                password = Password.getText().toString().trim();
//                email = Email.getText().toString().trim();
//
//                if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
//                    Toast.makeText(getApplicationContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                String role = "primary user";
//                roll = UUID.randomUUID().toString();
//                userdataholder obj = new userdataholder(roll,name, email, password, role);
//                FirebaseDatabase db = FirebaseDatabase.getInstance();
//                DatabaseReference node = db.getReference("user");
//                node.child(roll).setValue(obj);
//                node.child(roll).addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        Log.d("Firebase", "Data written successfully");
//                        Toast.makeText(signup.this, "Data written successfully", Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//                        Log.e("Firebase", "Error writing data: " + databaseError.getMessage());
//                        Toast.makeText(signup.this, "Failed to write data", Toast.LENGTH_SHORT).show();
//                    }
//                });
//
//
//                Name.setText("");
//                Password.setText("");
//                Email.setText("");
//                Toast.makeText(getApplicationContext(), "Registration Successful", Toast.LENGTH_SHORT).show();
//
//                Intent i = new Intent(signup.this, homepage.class);
//                startActivity(i);
//                finish();
//            }
//        });
//    }
//
//}