package com.superpowered.playerexample;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class MyViewHolder extends RecyclerView.ViewHolder {
    TextView song_name;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        song_name = itemView.findViewById(R.id.song_title);

    }
}