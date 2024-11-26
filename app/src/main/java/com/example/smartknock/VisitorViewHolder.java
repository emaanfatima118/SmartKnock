package com.example.smartknock;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class VisitorViewHolder extends RecyclerView.ViewHolder {
    TextView timeView,nameView;
    Button btn;
    public VisitorViewHolder(@NonNull View itemView) {
        super(itemView);
        timeView=itemView.findViewById(R.id.date);
        nameView=itemView.findViewById(R.id.name);
        btn=itemView.findViewById(R.id.visitor_detail);
    }
}
