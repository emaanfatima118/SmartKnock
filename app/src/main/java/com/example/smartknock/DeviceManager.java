package com.example.smartknock;

import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class DeviceManager {
    private final FirebaseFirestore db;

    public DeviceManager() {
        this.db = FirebaseFirestore.getInstance();
        // FirebaseApp.initializeApp(FirebaseApp.getInstance().getApplicationContext()); // Correct Firebase initialization
    }
    public void updateDevicePin(String deviceId, int newPin, DatabaseCallback callback) {
        // Ensure the deviceId is valid
        if (deviceId == null || deviceId.isEmpty()) {
            callback.onFailure("Device ID is null or empty");
            return;
        }

        // Reference to the "device" collection in Firestore
        CollectionReference devicesRef = db.collection("device");

        // Query the device by deviceId
        devicesRef.whereEqualTo("deviceId", deviceId)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        DocumentSnapshot document = querySnapshot.getDocuments().get(0); // Get the first matching document
                        String docId = document.getId(); // Get the Firestore document ID

                        // Update the PIN
                        Map<String, Object> updatedData = new HashMap<>();
                        updatedData.put("pin", newPin);

                        devicesRef.document(docId)
                                .update(updatedData)
                                .addOnSuccessListener(aVoid -> callback.onSuccess("Device PIN updated successfully"))
                                .addOnFailureListener(e -> callback.onFailure("Failed to update PIN: " + e.getMessage()));
                    } else {
                        callback.onFailure("Device with provided ID not found.");
                    }
                })
                .addOnFailureListener(e -> callback.onFailure("Error querying device: " + e.getMessage()));
    }

    // Add a device to Firestore
    public void addDevice(Device device, DatabaseCallback callback) {
        CollectionReference devicesRef = db.collection("Devices");

        Map<String, Object> deviceData = new HashMap<>();
        deviceData.put("deviceId", device.getDeviceId());
        deviceData.put("pin", device.getPin());

        devicesRef.document(device.getDeviceId())
                .set(deviceData)
                .addOnSuccessListener(aVoid -> callback.onSuccess("Device added successfully"))
                .addOnFailureListener(e -> callback.onFailure("Error adding device: " + e.getMessage()));
    }
    // Connect a device to a user
    public void connectDevice(String userId, Device device, Boolean status, DatabaseCallback callback) {
        // Reference the 'Devices' collection and query for a device with the specified PIN
        CollectionReference devicesRef = db.collection("device");

        devicesRef.whereEqualTo("pin", device.getPin()).get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        // Extract the Device ID from Firestore
                        DocumentSnapshot document = querySnapshot.getDocuments().get(0);
                        String deviceId = document.getId();

                        if (!deviceId.equals(device.getDeviceId())) {
                            callback.onFailure("Device ID mismatch. Provided ID does not match the Firestore record.");
                            return;
                        }

                        // Check if the user-device combination already exists
                        CollectionReference userDevicesRef = db.collection("UsersDevices");
                        userDevicesRef.whereEqualTo("userId", userId)
                                .whereEqualTo("deviceId", deviceId)
                                .get()
                                .addOnSuccessListener(userDeviceQuery -> {
                                    if (!userDeviceQuery.isEmpty()) {
                                        // Record exists - update the PIN if provided
                                        if (device.getPin() != null && device.getPin() != 0) {
                                            DocumentSnapshot existingRecord = userDeviceQuery.getDocuments().get(0);
                                            String recordId = existingRecord.getId();
                                            userDevicesRef.document(recordId)
                                                    .update("pin", device.getPin())
                                                    .addOnSuccessListener(aVoid -> callback.onSuccess("Device PIN updated successfully."))
                                                    .addOnFailureListener(e -> callback.onFailure("Failed to update device PIN: " + e.getMessage()));
                                        }
                                        else {
                                            callback.onSuccess("Device already connected to user. No changes made.");
                                        }
                                    } else {
                                        // Record does not exist - create a new record
                                        Map<String, Object> userDeviceData = new HashMap<>();
                                        userDeviceData.put("userId", userId);
                                        userDeviceData.put("deviceId", deviceId);


                                        userDevicesRef.add(userDeviceData)
                                                .addOnSuccessListener(aVoid -> callback.onSuccess("Device connected to user successfully"))
                                                .addOnFailureListener(e -> callback.onFailure("Failed to connect device: " + e.getMessage()));
                                    }
                                })
                                .addOnFailureListener(e -> callback.onFailure("Error checking user-device record: " + e.getMessage()));
                    } else {
                        callback.onFailure("Device with the provided PIN not found in Firestore.");
                    }
                })
                .addOnFailureListener(e -> callback.onFailure("Error fetching device data: " + e.getMessage()));
    }

/*
    // Connect a device to a user
    public void connectDevice(String userId, Device device, Boolean status, DatabaseCallback callback) {
        // Reference the 'Devices' collection and query for a device with the specified PIN
        CollectionReference devicesRef = db.collection("device");

        devicesRef.whereEqualTo("pin", device.getPin()).get()
                .addOnSuccessListener(querySnapshot -> {
                    if (!querySnapshot.isEmpty()) {
                        // Extract the Device ID from Firestore
                        DocumentSnapshot document = querySnapshot.getDocuments().get(0);
                        String deviceId = document.getId();
                        if (!deviceId.equals(device.getDeviceId())) {
                            callback.onFailure("Device ID mismatch. Provided ID does not match the Firestore record.");
                            return;
                        }
                        // Proceed to connect the device
                        CollectionReference userDevicesRef = db.collection("UsersDevices");

                        Map<String, Object> userDeviceData = new HashMap<>();
                        userDeviceData.put("userId", userId);
                        userDeviceData.put("deviceId", device.getDeviceId());
                        //userDeviceData.put("pin", device.getPin());
                      //  userDeviceData.put("status", status ? "active" : "inactive");

                        userDevicesRef.add(userDeviceData)
                                .addOnSuccessListener(aVoid -> callback.onSuccess("Device connected to user successfully"))
                                .addOnFailureListener(e -> callback.onFailure("Failed to connect device: " + e.getMessage()));
                    } else {
                        callback.onFailure("Device with the provided PIN not found in Firestore.");
                    }
                })
                .addOnFailureListener(e -> callback.onFailure("Error fetching device data: " + e.getMessage()));
    }*/

    // Function to disconnect a device by deleting it from Firestore
    public void disconnectDevice(String userId, String deviceId, DatabaseCallback callback) {
        System.out.println("Attempting to connect device with ID: " + deviceId);
        db.collection("UsersDevices")
                .whereEqualTo("userId", userId)
                .whereEqualTo("deviceId", deviceId)
                //  .whereEqualTo("status", "active")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        // Get the document reference for the device to disconnect
                        DocumentSnapshot document = task.getResult().getDocuments().get(0);

                        // Delete the document (disconnect the device)
                        db.collection("UsersDevices")
                                .document(document.getId())
                                .delete()
                                .addOnSuccessListener(aVoid -> {
                                    callback.onSuccess("Device disconnected successfully");
                                })
                                .addOnFailureListener(e -> {
                                    callback.onFailure("Error disconnecting device: " + e.getMessage());
                                });
                    } else {
                        callback.onFailure("Device not found or already disconnected");
                    }
                });
    }
}
/*
    // Link a device to a user in the Firestore
    public void connectDevice(String userId, Device device,Boolean status, DatabaseCallback callback) {
        CollectionReference userDevicesRef = db.collection("UsersDevices");

        Map<String, Object> userDeviceData = new HashMap<>();
        userDeviceData.put("userId", userId);
        userDeviceData.put("deviceId", device.getDeviceId());
        userDeviceData.put("pin", device.getPin());
     //   userDeviceData.put("status", "active");
        userDeviceData.put("status", status ? "active" : "inactive");
        userDevicesRef.add(userDeviceData)
                .addOnSuccessListener(aVoid -> callback.onSuccess("Device connected to user successfully"))
                .addOnFailureListener(e -> callback.onFailure(e.getMessage()));
    }*/
// Connect a device to a user