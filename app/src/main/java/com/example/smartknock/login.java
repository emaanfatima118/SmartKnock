package com.example.smartknock;

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
    UserController userController;

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

        // Initialize UserController
        userController = new UserController();

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
            userController.loginUser(userEmail, userPassword, new LoginCallback() {
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
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//
//import androidx.activity.EdgeToEdge;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.graphics.Insets;
//import androidx.core.view.ViewCompat;
//import androidx.core.view.WindowInsetsCompat;
//
//import com.google.firebase.auth.FirebaseAuth;
//
//public class login extends AppCompatActivity {
//    Button btn;
//    Button proceedbtn;
//    FirebaseAuth fb;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_login);
//        fb=FirebaseAuth.getInstance();
//        btn=findViewById(R.id.signupButton);
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i= new Intent(login.this, signup.class);
//                startActivity(i);
//                finish();
//            }
//        });
//        proceedbtn=findViewById(R.id.loginButton);
//        proceedbtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i= new Intent(login.this, homepage.class);
//                startActivity(i);
//                finish();
//            }
//        });
//    }
//}