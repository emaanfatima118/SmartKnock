package com.example.smartknock;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FeedbackAdapter extends RecyclerView.Adapter<FeedbackViewHolder> {

    private final Context context;
    private final List<FeedbackClass> feedbackList;

    public FeedbackAdapter(Context context, List<FeedbackClass> feedbackList) {
        this.context = context;
        this.feedbackList = feedbackList;
    }

    @NonNull
    @Override
    public FeedbackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FeedbackViewHolder(LayoutInflater.from(context).inflate(R.layout.single_feedback, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FeedbackViewHolder holder, int position) {
        FeedbackClass feedback = feedbackList.get(position);
        holder.userNameTextView.setText(feedback.getUserName());
        holder.feedbackMessageTextView.setText(feedback.getMessage());
        holder.ratingTextView.setText("Rating: " + feedback.getRating());
    }

    @Override
    public int getItemCount() {
        return feedbackList.size();
    }

    public void updateFeedbackList(List<FeedbackClass> updatedList) {
        feedbackList.clear();
        feedbackList.addAll(updatedList);
        notifyDataSetChanged();
    }
}
