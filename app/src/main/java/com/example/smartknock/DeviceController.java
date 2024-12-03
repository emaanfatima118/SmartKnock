package com.example.smartknock;



public class DeviceController {
    private final DeviceManager deviceManager;

    public DeviceController() {
        this.deviceManager = new DeviceManager();
    }

    // Register a new device
    public void registerDevice(String deviceId, int pin, DatabaseCallback callback) {
        Device device = new Device(deviceId, pin);
        deviceManager.addDevice(device, callback);
    }
    public void updateDevicePin(String deviceId, int newPin,int pin,Device device, DatabaseCallback callback) {
        deviceManager.updateDevicePin(deviceId, newPin, callback);
    }

    // Connect a device to a user
    public void connectDevice(String userId, String deviceId, int pin,Boolean status, DatabaseCallback callback) {
        Device device = new Device(deviceId, pin);
        deviceManager.connectDevice(userId, device, status, callback);
    }
    //discpnnect
    public void disconnectDevice(String userId, String deviceId, DatabaseCallback callback) {
        deviceManager.disconnectDevice(userId, deviceId, callback);
    }
}
