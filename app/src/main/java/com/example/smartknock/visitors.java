//package com.example.smartknock;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.util.Log;
//import android.view.View;
//import android.widget.EditText;
//import android.widget.ImageButton;
//import android.widget.Toast;
//
//import androidx.activity.EdgeToEdge;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.google.firebase.Timestamp;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import com.google.firebase.database.ChildEventListener;
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.firestore.QueryDocumentSnapshot;
//
//public class visitors extends AppCompatActivity {
//
//    private RecyclerView recyclerView;
//    private VisitorAdapter adapter;
//    private ArrayList<VisitorView> visitorsList;
//    private EditText searchBar;
//    private ImageButton searchButton, helpp, settings, feedback, logout, backButton;
//    VisitorController visitorController; // Controller for fetching data
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_visitors);
//
//        // Initialize UI components
//        recyclerView = findViewById(R.id.recyclerView);
//        searchBar = findViewById(R.id.searchBar);
//        searchButton = findViewById(R.id.searchButton);
//        helpp = findViewById(R.id.helpcenterButton);
//        settings = findViewById(R.id.settingsButton);
//        feedback = findViewById(R.id.feedbackButton);
//        logout = findViewById(R.id.logoutButton);
//        backButton = findViewById(R.id.homeButton);
//
//        // Set up RecyclerView
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        visitorsList = new ArrayList<>();
//        adapter = new VisitorAdapter(this, visitorsList);
//        recyclerView.setAdapter(adapter);
//
//        // Initialize the VisitorController
//        visitorController = new VisitorController();
//
//        // Fetch initial data from Firestore
//        fetchVisitorData();
//// Set up Realtime Database listener
//        setupRealtimeDatabaseListener();
//        backButton.setOnClickListener(view -> {
//            Intent intent = new Intent(visitors.this, homepage.class);
//            startActivity(intent);
//            finish();
//        });
//        helpp.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i = new Intent(visitors.this, helpcentre.class);
//                startActivity(i);
//                finish();
//            }
//        });
//
//        settings.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i = new Intent(visitors.this, settings.class);
//                startActivity(i);
//                finish();
//            }
//        });
//        feedback.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i = new Intent(visitors.this, feedback.class);
//                startActivity(i);
//                finish();
//            }
//        });
//        logout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Clear user session data from SharedPreferences (if used)
//                getSharedPreferences("UserSession", MODE_PRIVATE)
//                        .edit()
//                        .clear() // Clear all session-related data
//                        .apply(); // Apply changes
//
//                // Show a toast message to confirm logout
//                Toast.makeText(visitors.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
//
//                // Redirect user to the login screen
//                Intent intent = new Intent(visitors.this, login.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear activity stack
//                startActivity(intent);
//
//                // Finish the current activity
//                finish();
//            }
//        });
//        // Real-time search as user types
//        searchBar.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                adapter.filter(s.toString()); // Filter the list based on input
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//            }
//        });
//
//        // Search button click listener
//        searchButton.setOnClickListener(view -> {
//            String query = searchBar.getText().toString().trim();
//            if (!query.isEmpty()) {
//                adapter.filter(query); // Perform filtering when search button is clicked
//            } else {
//                Toast.makeText(visitors.this, "Enter a query to search", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//    @Override
//    protected void onResume() {
//        super.onResume();
//        // Re-fetch data and update the adapter
//        fetchVisitorData();
//    }
//    private void fetchVisitorData() {
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//
//        db.collection("visitors")
//                .get()
//                .addOnSuccessListener(querySnapshot -> {
//                    visitorsList.clear();
//                    for (QueryDocumentSnapshot document : querySnapshot) {
//                        String visitorName = document.getString("visitorName");
//                        Timestamp timestamp = document.getTimestamp("visitorDateTime");
//                        String imageString = document.getString("visitorImage");
//
//                        if (visitorName != null && timestamp != null) {
//                            Date dateTime = timestamp.toDate();
//                            VisitorView visitor = new VisitorView(document.getId(), false, dateTime, imageString);
//                            visitor.setName(visitorName);
//                            visitorsList.add(visitor);
//                        }
//                    }
//                    adapter.updateVisitors(visitorsList); // Notify adapter to update UI
//                    adapter.notifyDataSetChanged();
//                })
//                .addOnFailureListener(e -> Log.e("Firestore", "Error fetching visitors: " + e.getMessage()));
//    }
//
//
//    private void setupRealtimeDatabaseListener() {
//        DatabaseReference realtimeDbRef = FirebaseDatabase.getInstance().getReference("images");
//
//        realtimeDbRef.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot snapshot, String previousChildName) {
//                String imageString = snapshot.getValue(String.class); // Image URL or Base64
//                String visitorId = snapshot.getKey();
//
//                // Check if visitor already exists in Firestore to avoid duplicates
//                boolean alreadyExists = false;
//                for (VisitorView visitor : visitorsList) {
//                    if (visitor.getId().equals(visitorId)) {
//                        alreadyExists = true;
//                        break;
//                    }
//                }
//
//                if (!alreadyExists && visitorId != null && imageString != null) {
//                    Date timestamp = new Date(); // Assign current time for new visitors only
//                    pushVisitorToFirestore(visitorId, imageString, timestamp);
//                }
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot snapshot, String previousChildName) {}
//
//            @Override
//            public void onChildRemoved(DataSnapshot snapshot) {}
//
//            @Override
//            public void onChildMoved(DataSnapshot snapshot, String previousChildName) {}
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//                Log.e("RealtimeDB", "Error listening for changes: " + error.getMessage());
//            }
//        });
//    }
//
//
//
//    // Push new visitor data to Firestore
//// Push new visitor data to Firestore
//    private void pushVisitorToFirestore(String visitorId, String imageString, Date timestamp) {
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//
//        // Default visitor name (can be updated later with actual name if available)
//        String visitorName = "Unknown Visitor";
//
//        // Create a map to hold the visitor data
//        Map<String, Object> visitorData = new HashMap<>();
//        visitorData.put("visitorName", visitorName);
//        visitorData.put("visitorDateTime", new Timestamp(timestamp));
//        visitorData.put("visitorImage", imageString);
//
//        // Push the data to Firestore using the visitor's ID as the document ID
//        db.collection("visitors").document(visitorId)
//                .set(visitorData)
//                .addOnSuccessListener(aVoid -> {
//                    Log.d("Firestore", "New visitor added successfully");
//
//                    // Add visitor to the RecyclerView list and notify adapter
//                    VisitorView newVisitor = new VisitorView(visitorId, false, timestamp, imageString);
//                    newVisitor.setName(visitorName);
//                    visitorsList.add(newVisitor);
//                    adapter.notifyDataSetChanged();
//                })
//                .addOnFailureListener(e -> Log.e("Firestore", "Error adding visitor: " + e.getMessage()));
//    }
//
//}

package com.example.smartknock;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class visitors extends AppCompatActivity {

    private RecyclerView recyclerView;
    private VisitorAdapter adapter;
    private ArrayList<VisitorView> visitorsList;
    private EditText searchBar;
    private ImageButton searchButton, helpp, settings, feedback, logout, backButton;
    VisitorController visitorController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_visitors);

        // Initialize UI components
        recyclerView = findViewById(R.id.recyclerView);
        searchBar = findViewById(R.id.searchBar);
        searchButton = findViewById(R.id.searchButton);
        helpp = findViewById(R.id.helpcenterButton);
        settings = findViewById(R.id.settingsButton);
        feedback = findViewById(R.id.feedbackButton);
        logout = findViewById(R.id.logoutButton);
        backButton = findViewById(R.id.homeButton);

        // Set up RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        visitorsList = new ArrayList<>();
        adapter = new VisitorAdapter(this, visitorsList);
        recyclerView.setAdapter(adapter);

        // Initialize the VisitorController
        visitorController = new VisitorController();

        // Fetch initial data from Firestore
        fetchVisitorData();

        // Set up Realtime Database listener
        setupRealtimeDatabaseListener();

        // Handle navigation and interaction
        setupNavigation();

        // Real-time search as user types
        setupSearchBar();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Re-fetch data and update the adapter
        fetchVisitorData();
    }

    private void fetchVisitorData() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("visitors")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    visitorsList.clear();
                    for (QueryDocumentSnapshot document : querySnapshot) {
                        String visitorName = document.getString("visitorName");
                        Timestamp timestamp = document.getTimestamp("visitorDateTime");
                        String imageString = document.getString("visitorImage");

                        if (visitorName != null && timestamp != null) {
                            Date dateTime = timestamp.toDate();
                            VisitorView visitor = new VisitorView(document.getId(), false, dateTime, imageString);
                            visitor.setName(visitorName);
                            visitorsList.add(visitor);
                        }
                    }
                    adapter.updateVisitors(visitorsList); // Notify adapter to update UI
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> Log.e("Firestore", "Error fetching visitors: " + e.getMessage()));
    }

    private void setupRealtimeDatabaseListener() {
        DatabaseReference realtimeDbRef = FirebaseDatabase.getInstance().getReference("images");

        realtimeDbRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot snapshot, String previousChildName) {
                String imageString = snapshot.getValue(String.class);
                String visitorId = snapshot.getKey();

                boolean alreadyExists = false;
                for (VisitorView visitor : visitorsList) {
                    if (visitor.getId().equals(visitorId)) {
                        alreadyExists = true;
                        break;
                    }
                }

                if (!alreadyExists && visitorId != null && imageString != null) {
                    Date timestamp = new Date();
                    pushVisitorToFirestore(visitorId, imageString, timestamp);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot snapshot, String previousChildName) {}

            @Override
            public void onChildRemoved(DataSnapshot snapshot) {}

            @Override
            public void onChildMoved(DataSnapshot snapshot, String previousChildName) {}

            @Override
            public void onCancelled(DatabaseError error) {
                Log.e("RealtimeDB", "Error listening for changes: " + error.getMessage());
            }
        });
    }

//    private void pushVisitorToFirestore(String visitorId, String imageString, Date timestamp) {
//        FirebaseFirestore db = FirebaseFirestore.getInstance();
//
//        String visitorName = "Unknown Visitor";
//        Map<String, Object> visitorData = new HashMap<>();
//        visitorData.put("visitorName", visitorName);
//        visitorData.put("visitorDateTime", new Timestamp(timestamp));
//        visitorData.put("visitorImage", imageString);
//
//        db.collection("visitors").document(visitorId)
//                .set(visitorData)
//                .addOnSuccessListener(aVoid -> {
//                    Log.d("Firestore", "New visitor added successfully");
//
//                    VisitorView newVisitor = new VisitorView(visitorId, false, timestamp, imageString);
//                    newVisitor.setName(visitorName);
//                    visitorsList.add(newVisitor);
//                    adapter.notifyDataSetChanged();
//                })
//                .addOnFailureListener(e -> Log.e("Firestore", "Error adding visitor: " + e.getMessage()));
//    }
private void pushVisitorToFirestore(String visitorId, String imageString, Date timestamp) {
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    // Check if the visitor already exists in Firestore
    db.collection("visitors").document(visitorId).get()
            .addOnSuccessListener(documentSnapshot -> {
                if (documentSnapshot.exists()) {
                    // Visitor already exists, avoid overwriting the visit time
                    Log.d("Firestore", "Visitor already exists, skipping update for visitorDateTime");
                } else {
                    // Visitor doesn't exist, create new entry
                    String visitorName = "Unknown Visitor"; // Default name until updated
                    Map<String, Object> visitorData = new HashMap<>();
                    visitorData.put("visitorName", visitorName);
                    visitorData.put("visitorDateTime", new Timestamp(timestamp));
                    visitorData.put("visitorImage", imageString);

                    // Push the data to Firestore using the visitor's ID as the document ID
                    db.collection("visitors").document(visitorId)
                            .set(visitorData)
                            .addOnSuccessListener(aVoid -> {
                                Log.d("Firestore", "New visitor added successfully");

                                // Add the new visitor to the RecyclerView list and notify the adapter
                                VisitorView newVisitor = new VisitorView(visitorId, false, timestamp, imageString);
                                newVisitor.setName(visitorName);
                                visitorsList.add(newVisitor);
                                adapter.notifyDataSetChanged();
                            })
                            .addOnFailureListener(e -> Log.e("Firestore", "Error adding visitor: " + e.getMessage()));
                }
            })
            .addOnFailureListener(e -> Log.e("Firestore", "Error checking if visitor exists: " + e.getMessage()));
}

    private void setupSearchBar() {
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        searchButton.setOnClickListener(view -> {
            String query = searchBar.getText().toString().trim();
            if (!query.isEmpty()) {
                adapter.filter(query);
            } else {
                Toast.makeText(visitors.this, "Enter a query to search", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupNavigation() {
        backButton.setOnClickListener(view -> {
            Intent intent = new Intent(visitors.this, homepage.class);
            startActivity(intent);
            finish();
        });

        helpp.setOnClickListener(view -> {
            Intent intent = new Intent(visitors.this, helpcentre.class);
            startActivity(intent);
            finish();
        });

        settings.setOnClickListener(view -> {
            Intent intent = new Intent(visitors.this, settings.class);
            startActivity(intent);
            finish();
        });

        feedback.setOnClickListener(view -> {
            Intent intent = new Intent(visitors.this, feedback.class);
            startActivity(intent);
            finish();
        });

        logout.setOnClickListener(view -> {
            getSharedPreferences("UserSession", MODE_PRIVATE).edit().clear().apply();
            Toast.makeText(visitors.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(visitors.this, login.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }
}
