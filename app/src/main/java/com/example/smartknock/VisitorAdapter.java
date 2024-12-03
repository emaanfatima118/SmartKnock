//package com.example.smartknock;
//
//import android.content.Context;
//import android.content.Intent;
//import android.view.LayoutInflater;
//import android.view.ViewGroup;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import java.text.SimpleDateFormat;
//import java.util.List;
//import java.util.Locale;
//
//public class VisitorAdapter extends RecyclerView.Adapter<VisitorViewHolder> {
//
//    private final Context context;
//    private final List<VisitorView> visitors;
//
//    public VisitorAdapter(Context context, List<VisitorView> visitors) {
//        this.context = context;
//        this.visitors = visitors;
//    }
//
//    @NonNull
//    @Override
//    public VisitorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        return new VisitorViewHolder(LayoutInflater.from(context).inflate(R.layout.single_visitor, parent, false));
//    }
//
//@Override
//public void onBindViewHolder(@NonNull VisitorViewHolder holder, int position) {
//    VisitorView visitor = visitors.get(position);
//    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
//    String formattedDate = visitor.getDatetime() != null ? sdf.format(visitor.getDatetime()) : "No Date";
//
//    holder.timeView.setText(formattedDate);
//    holder.nameView.setText(visitor.getName() != null ? visitor.getName() : "Unknown Visitor");
//
//    holder.btn.setOnClickListener(v -> {
//        Intent intent = new Intent(context, visitorDetails.class);
//        intent.putExtra("visitorId", visitor.getId());
//        intent.putExtra("visitorName", visitor.getName());
//        intent.putExtra("visitorImageUrl", visitor.getImageUrl());
//        intent.putExtra("visitorDateTime", formattedDate);
//        context.startActivity(intent);
//    });
//}
//
//
//    @Override
//    public int getItemCount() {
//        return visitors.size();
//    }
//}
package com.example.smartknock;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class VisitorAdapter extends RecyclerView.Adapter<VisitorViewHolder> {

    private final Context context;
    private List<VisitorView> visitors; // Current list displayed
    private final List<VisitorView> originalVisitors; // Original list for filtering

    public VisitorAdapter(Context context, List<VisitorView> visitors) {
        this.context = context;
        this.visitors = new ArrayList<>(visitors);
        this.originalVisitors = new ArrayList<>(visitors);
    }

    @NonNull
    @Override
    public VisitorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VisitorViewHolder(LayoutInflater.from(context).inflate(R.layout.single_visitor, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VisitorViewHolder holder, int position) {
        VisitorView visitor = visitors.get(position);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String formattedDate = visitor.getDatetime() != null ? sdf.format(visitor.getDatetime()) : "No Date";

        holder.timeView.setText(formattedDate);
        holder.nameView.setText(visitor.getName() != null ? visitor.getName() : "Unknown Visitor");

        holder.btn.setOnClickListener(v -> {
            Intent intent = new Intent(context, visitorDetails.class);
            intent.putExtra("visitorId", visitor.getId());
            intent.putExtra("visitorName", visitor.getName());
            intent.putExtra("visitorImageUrl", visitor.getImageUrl());
            intent.putExtra("visitorDateTime", formattedDate);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return visitors.size();
    }

    /**
     * Filter visitors by name or timestamp.
     *
     * @param query The search query entered by the user.
     */
    public void filter(String query) {
        query = query.toLowerCase(Locale.getDefault()).trim();
        visitors.clear();

        if (query.isEmpty()) {
            visitors.addAll(originalVisitors); // Show all visitors if query is empty
        } else {
            for (VisitorView visitor : originalVisitors) {
                boolean nameMatches = visitor.getName() != null && visitor.getName().toLowerCase(Locale.getDefault()).contains(query);

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                String formattedDate = visitor.getDatetime() != null ? sdf.format(visitor.getDatetime()).toLowerCase(Locale.getDefault()) : "";

                boolean timestampMatches = formattedDate.contains(query);

                if (nameMatches || timestampMatches) {
                    visitors.add(visitor);
                }
            }
        }

        notifyDataSetChanged(); // Refresh RecyclerView
    }

    /**
     * Update the adapter with a new list of visitors.
     *
     * @param newVisitors The new list of visitors to display.
     */
    public void updateVisitors(List<VisitorView> newVisitors) {
        originalVisitors.clear();
        originalVisitors.addAll(newVisitors);

        visitors.clear();
        visitors.addAll(newVisitors);

        notifyDataSetChanged(); // Refresh RecyclerView
    }
}
