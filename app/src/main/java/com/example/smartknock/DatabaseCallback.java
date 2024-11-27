package com.example.smartknock;


public interface DatabaseCallback {
    void onSuccess(String message);
    void onFailure(String error);
}

