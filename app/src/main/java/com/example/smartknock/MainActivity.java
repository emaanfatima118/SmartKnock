package com.example.smartknock;
import com.google.firebase.FirebaseApp;
//import com.google.firebase.messaging.FirebaseMessaging;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        btn=findViewById(R.id.nextt);
        // Initialize Firebase
        if (FirebaseApp.getApps(this).isEmpty()) {
            FirebaseApp.initializeApp(this);
        }
//        FirebaseMessaging.getInstance().getToken()
//                .addOnCompleteListener(task -> {
//                    if (!task.isSuccessful()) {
//                        Log.w("FCM", "Fetching FCM registration token failed", task.getException());
//                        return;
//                    }
//
//                    // Get the FCM token
//                    String token = task.getResult();
//
//                    // Log and save the token (you can send it to your server or use it to subscribe to topics)
//                    Log.d("FCM", "FCM Token: " + token);
//                });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(MainActivity.this, login.class);
                startActivity(i);
                finish();
            }
        });
        //
//        FirebaseMessaging.getInstance().subscribeToTopic("visitor_updates")
//                .addOnCompleteListener(task -> {
//                    if (task.isSuccessful()) {
//                        Log.d("FCM", "Subscribed to visitor updates successfully");
//                    } else {
//                        Log.e("FCM", "Failed to subscribe to visitor updates", task.getException());
//                    }
//                });

    }

}