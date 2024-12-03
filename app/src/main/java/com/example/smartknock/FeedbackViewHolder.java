package com.example.smartknock;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class FeedbackViewHolder extends RecyclerView.ViewHolder {

    TextView userNameTextView;
    TextView feedbackMessageTextView;
    TextView ratingTextView;

    public FeedbackViewHolder(View itemView) {
        super(itemView);

        userNameTextView = itemView.findViewById(R.id.user_name); // Reference to the user name TextView
        feedbackMessageTextView = itemView.findViewById(R.id.feedback_message); // Reference to the feedback message TextView
        ratingTextView = itemView.findViewById(R.id.rating); // Reference to the rating TextView
    }
}
