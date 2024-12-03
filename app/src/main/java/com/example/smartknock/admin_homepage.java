package com.example.smartknock;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class admin_homepage extends AppCompatActivity {

    ImageButton logout,feedback;
        private RecyclerView recyclerView;
        private UserAdapter userAdapter;
        private List<User> userList;
        private AuthenticationHelper authhelp;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            EdgeToEdge.enable(this);
            setContentView(R.layout.activity_admin_homepage);

            recyclerView = findViewById(R.id.recyclerView);
            Button addUserButton = findViewById(R.id.addUserButton);
            Button removeUserButton = findViewById(R.id.removeUserButton);

            // Initialize the user list and adapter
            userList = new ArrayList<>();
            userAdapter = new UserAdapter(userList);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(userAdapter);
            logout = findViewById(R.id.logoutButton);
            feedback = findViewById(R.id.feedbackButton);
            // Fetch users from Firestore

            authhelp= new AuthenticationHelper();
            authhelp.readUsers(new AuthenticationHelper.DataStatus() {
                @Override
                public void DataIsLoaded(List<User> users, String message) {
                    if (users != null) {
                        userList.clear();
                        userList.addAll(users);
                        userAdapter.notifyDataSetChanged();
                    }
                    Toast.makeText(admin_homepage.this, message, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onFailure(String message) {
                    Toast.makeText(admin_homepage.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onSuccess(String s) {
                    // Handle success case if needed
                    // For example, show a Toast message
                    Toast.makeText(admin_homepage.this, s, Toast.LENGTH_SHORT).show();
                }
            });
            feedback.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(admin_homepage.this, feedbackadmin.class);
                    startActivity(i);
                    finish();
                }
            });
            logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Clear user session data from SharedPreferences (if used)
                    getSharedPreferences("UserSession", MODE_PRIVATE)
                            .edit()
                            .clear() // Clear all session-related data
                            .apply(); // Apply changes

                    // Show a toast message to confirm logout
                    Toast.makeText(admin_homepage.this, "Logged out successfully", Toast.LENGTH_SHORT).show();

                    // Redirect user to the login screen
                    Intent intent = new Intent(admin_homepage.this, login.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Clear activity stack
                    startActivity(intent);

                    // Finish the current activity
                    finish();
                }
            });
            // Add user button click listener
            addUserButton.setOnClickListener(v -> {
                // Navigate to the adduser activity
                Intent intent = new Intent(admin_homepage.this, adduser.class);
                startActivity(intent);
            });

            removeUserButton.setOnClickListener(v -> {
                if (!userList.isEmpty()) {
                    // Display a dialog to select the user to remove
                    CharSequence[] userNames = new CharSequence[userList.size()];
                    for (int i = 0; i < userList.size(); i++) {
                        userNames[i] = userList.get(i).getName(); // Assuming User has a getName() method
                    }

                    AlertDialog.Builder builder = new AlertDialog.Builder(admin_homepage.this);
                    builder.setTitle("Select a user to remove");
                    builder.setItems(userNames, (dialog, which) -> {
                        // Remove the selected user
                        User selectedUser = userList.get(which);
                        userList.remove(which);
                        userAdapter.notifyItemRemoved(which);

                        authhelp.deleteUser(selectedUser.getId(), new AuthenticationHelper.DataStatus() {
                            @Override
                            public void DataIsLoaded(List<User> users, String message) {

                            }

                            @Override
                            public void onFailure(String message) {
                                Toast.makeText(admin_homepage.this, "Error removing user: " + message, Toast.LENGTH_SHORT).show();

                            }

                            @Override
                            public void onSuccess(String s) {
                                Toast.makeText(admin_homepage.this, "User removed successfully.", Toast.LENGTH_SHORT).show();

                            }
                        });

                    });
                    builder.setNegativeButton("Cancel", null);
                    builder.show();
                } else {
                    Toast.makeText(this, "No users to remove!", Toast.LENGTH_SHORT).show();
                }
            });
        }

        // Method to refresh the user list from Firestore
        public void refreshUserList(List<User> updatedUserList) {
            userList.clear();
            userList.addAll(updatedUserList);
            userAdapter.notifyDataSetChanged();
        }
    }
