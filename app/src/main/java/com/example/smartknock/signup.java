package com.example.smartknock;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.UUID;

public class signup extends AppCompatActivity {
    Button btn;
    Button proceedbtn;
    String roll,name,password,email;
    EditText Name,Email,Password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (FirebaseApp.getApps(this).isEmpty()) {
            FirebaseApp.initializeApp(this);
        }

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);
        Email = findViewById(R.id.signupEmail);
        Password = findViewById(R.id.signupPassword);
        Name = findViewById(R.id.signupUsername);

        btn=findViewById(R.id.loginButton);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(signup.this, login.class);
                Log.d("Firebase", "Data written successfully");
                startActivity(i);
                finish();
            }
        });
        proceedbtn=findViewById(R.id.signupButton);
        proceedbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                name = Name.getText().toString().trim();
                password = Password.getText().toString().trim();
                email = Email.getText().toString().trim();

                if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }
                String role = "primary user";
                roll = UUID.randomUUID().toString();
                userdataholder obj = new userdataholder(roll,name, email, password, role);
                FirebaseDatabase db = FirebaseDatabase.getInstance();
                DatabaseReference node = db.getReference("user");
                node.child(roll).setValue(obj);
                node.child(roll).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Log.d("Firebase", "Data written successfully");
                        Toast.makeText(signup.this, "Data written successfully", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.e("Firebase", "Error writing data: " + databaseError.getMessage());
                        Toast.makeText(signup.this, "Failed to write data", Toast.LENGTH_SHORT).show();
                    }
                });


                Name.setText("");
                Password.setText("");
                Email.setText("");
                Toast.makeText(getApplicationContext(), "Registration Successful", Toast.LENGTH_SHORT).show();

                Intent i = new Intent(signup.this, homepage.class);
                startActivity(i);
                finish();
            }
        });
    }

}