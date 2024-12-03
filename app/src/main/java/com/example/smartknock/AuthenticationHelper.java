package com.example.smartknock;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AuthenticationHelper {

    private DatabaseReference databaseReference;
    private final FirebaseFirestore db;

    public AuthenticationHelper() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference();
        db = FirebaseFirestore.getInstance();
    }
    public interface DataStatus {
        void DataIsLoaded(List<User> users, String message);
        void onFailure(String message);
        void onSuccess(String s); // This method can be used for success notifications
    }
    public void readUsers(final DataStatus dataStatus) {
        db.collection("users")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<User> users = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            User user = document.toObject(User.class);
                            users.add(user);
                        }
                        dataStatus.DataIsLoaded(users, "Data loaded successfully");
                    } else {
                        dataStatus.onFailure("Failed to load data: " + task.getException().getMessage());
                    }
                });
    }
    public void deleteUser(String userId, DataStatus callback) {
        db.collection("users").document(userId)
                .delete()
                .addOnSuccessListener(aVoid -> callback.DataIsLoaded(null, "User deleted successfully"))
                .addOnFailureListener(e -> callback.onFailure("Error deleting user: " + e.getMessage()));
    }
    public void addUser(Map<String, Object> user, DataStatus callback) {
        db.collection("users").add(user)
                .addOnSuccessListener(documentReference -> {
                    if (callback != null) {
                        callback.onSuccess("User added successfully!");
                    }
                })
                .addOnFailureListener(e -> {
                    if (callback != null) {
                        callback.onFailure("Error adding user: " + e.getMessage());
                    }
                });
    }
    public void saveUser(String userId, Object user, DatabaseCallback callback) {
        db.collection("users").document(userId)
                .set(user)
                .addOnSuccessListener(aVoid -> callback.onSuccess("User saved successfully"))
                .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
    }

    public void saveAdmin(String adminId, Object admin, DatabaseCallback callback) {
        db.collection("admin").document(adminId)
                .set(admin)
                .addOnSuccessListener(aVoid -> callback.onSuccess("Admin saved successfully"))
                .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
    }

    public void saveDevice(String deviceId, Object device, DatabaseCallback callback) {
        db.collection("devices").document(deviceId)
                .set(device)
                .addOnSuccessListener(aVoid -> callback.onSuccess("Device saved successfully"))
                .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
    }
    public void getUserById(String userId, DatabaseCallback callback) {
        db.collection("users").document(userId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String userType = documentSnapshot.getString("userType");
                        String userName = documentSnapshot.getString("name");
                        callback.onSuccess("Login successful for: " + userName + " (" + userType + ")");
                    } else {
                        callback.onFailure("User not found in 'users' collection.");
                    }
                })
                .addOnFailureListener(e -> callback.onFailure("Error fetching user data: " + e.getMessage()));
    }
    public void getAdminById(String userId, DatabaseCallback callback) {
        db.collection("admin").document(userId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String adminName = documentSnapshot.getString("name");
                        callback.onSuccess("Login successful for admin: " + adminName);
                    } else {
                        callback.onFailure("Admin not found in 'admin' collection.");
                    }
                })
                .addOnFailureListener(e -> callback.onFailure("Error fetching admin data: " + e.getMessage()));
    }
}

