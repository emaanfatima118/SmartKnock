package com.example.smartknock;

import java.util.Date;

public class VisitorController {
    private final VisitorManager visitorManager;

    public VisitorController() {
        visitorManager = VisitorManager.getInstance(); // Singleton pattern
    }

    public void getAllVisitors(VisitorCallback callback) {
        visitorManager.fetchVisitors(callback);
    }

    public void updateVisitorName(String visitorId, String newName, VisitorCallback callback) {
        visitorManager.updateVisitorName(visitorId, newName, callback);
    }

    public void getVisitorById(String visitorId, VisitorCallback callback) {
        visitorManager.getVisitorById(visitorId, callback);
    }

    public void addVisitor(String name, boolean isFrequent, Date visitTime, String imageUrl) {
        visitorManager.addNewVisitor(name, isFrequent, visitTime, imageUrl);
    }
}
