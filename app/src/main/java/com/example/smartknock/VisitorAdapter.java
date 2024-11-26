package com.example.smartknock;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
public class VisitorAdapter extends RecyclerView.Adapter<VisitorViewHolder> {
    private Context context;
    private List<VisitorView> visitors;

    public VisitorAdapter(Context context, List<VisitorView> visitors) {
        this.context = context;
        this.visitors = visitors;
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
        String formattedDate = sdf.format(visitor.getDatetime());

        holder.timeView.setText(formattedDate);
        holder.nameView.setText(visitor.getName());

        holder.btn.setOnClickListener(v -> {
            // Navigate to VisitorDetails
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
}
