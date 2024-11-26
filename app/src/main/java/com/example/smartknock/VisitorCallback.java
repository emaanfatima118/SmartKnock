package com.example.smartknock;

import java.util.List;

public interface VisitorCallback {

    void onVisitorFetched(VisitorView visitor);          // For single visitor
    void onVisitorsFetched(List<VisitorView> visitors);  // For a list of visitors
    void onError(String errorMessage);                   // For errorsrMessage);
    }


