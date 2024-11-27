package com.example.smartknock;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.RecyclerView;
/*
import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class feedbackadmin extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FeedbackAdapter feedbackAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        fetchFeedbacks();
    }

    private void fetchFeedbacks() {
        DBHelper dbHelper = new DBHelper();

        dbHelper.fetchFeedbacks(new DBHelper.FeedbackCallback() {
            @Override
            public void onSuccess(List<Feedback> feedbackList) {
                feedbackAdapter = new FeedbackAdapter(feedbackList);
                recyclerView.setAdapter(feedbackAdapter);
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(feedbackadmin.this, "Failed to fetch feedbacks: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
*/