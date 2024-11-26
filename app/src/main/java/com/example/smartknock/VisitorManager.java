//package com.example.smartknock;
//
//import androidx.annotation.NonNull;
//
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import java.util.UUID;
//
//public class VisitorManager {
//    private static VisitorManager instance;
//    private List<VisitorView> visitors;
//    private DatabaseHelper databaseHelper;
//
//    private VisitorManager() {
//        visitors = new ArrayList<VisitorView>();
//        databaseHelper = new DatabaseHelper("visitors"); // Connect to Firebase or local DB
//    }
//
//    public List<VisitorView> getVisitors() {
//        return visitors;
//    }
//
//    public void setVisitors(List<VisitorView> visitors) {
//        this.visitors = visitors;
//    }
//    public VisitorView createVisitor(String name, Date date,String url, boolean isFrequent) {
//        String id = UUID.randomUUID().toString(); // Generate a unique ID
//        VisitorView visitor = new VisitorView(id,isFrequent,date,url);
//        visitor.setName(name);
//        visitors.add(visitor);
//        return visitor;
//    }
//    public static synchronized VisitorManager getInstance() {
//        if (instance == null) {
//            instance = new VisitorManager();
//        }
//        return instance;
//    }
//    public void updateVisitorName(String visitorId, String newName, VisitorCallback callback) {
//        databaseHelper.getDatabaseReference("visitors").child(visitorId).child("name")
//                .setValue(newName)
//                .addOnSuccessListener(aVoid -> {
//                    // Notify success
//                    callback.onVisitorFetched(null); // No need to fetch the updated visitor here
//                })
//                .addOnFailureListener(e -> {
//                    // Notify failure
//                    callback.onError("Failed to update name: " + e.getMessage());
//                });
//    }
//    public void fetchVisitors(VisitorCallback callback) {
//        // Fetch visitors from Firebase
//
//        databaseHelper.getDatabaseReference("visitors").addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                List<VisitorView> visitorList = new ArrayList<VisitorView>();
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    VisitorView visitor = snapshot.getValue(VisitorView.class);
//                    if (visitor != null) {
//                        visitorList.add(visitor);
//                    }
//                }
//                callback.onVisitorsFetched(visitorList);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                callback.onError("Error fetching data");
//            }
//        });
//    }
//    public void getVisitorById(String visitorId, VisitorCallback callback) {
//        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("visitors");
//
//        // Fetch the visitor by their ID
//        databaseReference.child(visitorId).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                // Check if the snapshot exists and contains data
//                if (snapshot.exists()) {
//                    VisitorView visitor = snapshot.getValue(VisitorView.class); // Map Firebase data to VisitorView
//                    if (visitor != null) {
//                        callback.onVisitorFetched(visitor); // Return the visitor via callback
//                    } else {
//                        callback.onError("Visitor data is malformed"); // Handle case where data is not properly mapped
//                    }
//                } else {
//                    callback.onError("Visitor not found"); // Visitor ID not found in the database
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                // Handle error scenario
//                callback.onError("Failed to fetch visitor: " + error.getMessage());
//            }
//        });
//    }
//
//
//    public void addNewVisitor(String name, boolean isFrequent, Date visitTime, String imageUrl) {
//        VisitorView visitor = new VisitorView(UUID.randomUUID().toString(), isFrequent, visitTime, imageUrl);
//        visitor.setName(name);
//        visitors.add(visitor);
//        databaseHelper.getDatabaseReference("visitors").child(visitor.getId()).setValue(visitor);
//    }
//}


package com.example.smartknock;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class VisitorManager {
    private static VisitorManager instance;
    private final List<VisitorView> visitors;
    private final DatabaseHelper databaseHelper;

    private VisitorManager() {
        visitors = new ArrayList<>();
        databaseHelper = new DatabaseHelper(); // Firestore-based helper
    }

    public static synchronized VisitorManager getInstance() {
        if (instance == null) {
            instance = new VisitorManager();
        }
        return instance;
    }

    public void fetchVisitors(VisitorCallback callback) {
        databaseHelper.getCollectionReference("visitors").get()
                .addOnSuccessListener(querySnapshot -> {
                    List<VisitorView> visitorList = new ArrayList<>();
                    for (DocumentSnapshot document : querySnapshot) {
                        VisitorView visitor = mapDocumentToVisitor(document);
                        if (visitor != null) {
                            visitorList.add(visitor);
                        }
                    }
                    callback.onVisitorsFetched(visitorList);
                })
                .addOnFailureListener(e -> callback.onError("Error fetching visitors: " + e.getMessage()));
    }

    public void getVisitorById(String visitorId, VisitorCallback callback) {
        databaseHelper.getCollectionReference("visitors").document(visitorId).get()
                .addOnSuccessListener(document -> {
                    if (document.exists()) {
                        VisitorView visitor = mapDocumentToVisitor(document);
                        if (visitor != null) {
                            callback.onVisitorFetched(visitor);
                        } else {
                            callback.onError("Error: Visitor data is malformed");
                        }
                    } else {
                        callback.onError("Error: Visitor not found");
                    }
                })
                .addOnFailureListener(e -> callback.onError("Error fetching visitor: " + e.getMessage()));
    }

    public void updateVisitorName(String visitorId, String newName, VisitorCallback callback) {
        databaseHelper.getCollectionReference("visitors").document(visitorId)
                .update("visitorName", newName)
                .addOnSuccessListener(aVoid -> callback.onVisitorFetched(null)) // Successfully updated
                .addOnFailureListener(e -> callback.onError("Error updating name: " + e.getMessage()));
    }

    public void addNewVisitor(String name, boolean isFrequent, Date visitTime, String imageUrl) {
        String id = UUID.randomUUID().toString();
        VisitorView visitor = new VisitorView(id, isFrequent, visitTime, imageUrl);
        visitor.setName(name);

        databaseHelper.getCollectionReference("visitors").document(visitor.getId()).set(visitor)
                .addOnSuccessListener(aVoid -> {
                    visitors.add(visitor); // Add to local list for consistency
                })
                .addOnFailureListener(e -> {
                    // Handle failure (e.g., log the error or retry mechanism)
                });
    }

    private VisitorView mapDocumentToVisitor(DocumentSnapshot document) {
        try {
            String id = document.getId();
            String name = document.getString("visitorName");
            boolean isFrequent = document.getBoolean("isFrequent") != null ? document.getBoolean("isFrequent") : false;
            Timestamp timestamp = document.getTimestamp("visitorDateTime");
            String imageUrl = document.getString("visitorImageUrl");
            Date visitTime = timestamp != null ? timestamp.toDate() : null;

            VisitorView visitor = new VisitorView(id, isFrequent, visitTime, imageUrl);
            visitor.setName(name);
            return visitor;
        } catch (Exception e) {
            // Handle mapping errors if fields are missing
            return null;
        }
    }
}
