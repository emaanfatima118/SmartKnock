package com.example.smartknock;


import java.util.List;

public class Device {
    private String deviceId;
    private String pin;
    private List<String> users;

    // No-arg constructor for Firestore deserialization
    public Device() {}

    // Constructor
    public Device(String deviceId, String pin, List<String> users) {
        this.deviceId = deviceId;
        this.pin = pin;
        this.users = users;
    }

    // Getters and setters
    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public List<String> getUsers() {
        return users;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }
}
