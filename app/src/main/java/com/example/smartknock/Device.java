package com.example.smartknock;

public class Device {
    private String deviceId;

    private Integer pin;
    private Boolean status;

    public Boolean getStatus() {
        return status;
    }


    // Constructor
    public Device(String deviceId, Integer pin) {
        this.deviceId = deviceId;
        this.pin = pin;

    }

    // Getters and Setters
    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public Integer getPin() {
        return pin;
    }

    public void setPin(Integer pin) {
        this.pin = pin;
    }
}
