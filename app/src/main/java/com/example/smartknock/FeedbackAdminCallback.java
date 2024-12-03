package com.example.smartknock;

import java.util.List;

public interface FeedbackAdminCallback {
    void onSuccess(List<FeedbackClass> feedbackList);
    void onFailure(Exception e);
}
