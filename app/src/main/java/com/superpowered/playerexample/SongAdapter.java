package com.superpowered.playerexample;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.ViewHolder> {

    private ArrayList<SongData> songs;

    public SongAdapter(ArrayList<SongData> songs) {
        this.songs = songs;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.songlist_holder,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            holder.bind(songs.get(position).song_name);
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView song_name;
        ViewHolder(View itemview)
        {
            super(itemview);
            song_name = itemview.findViewById(R.id.song_name);
        }

        void bind(String name)
        {
            song_name.setText(name);

        }


    }
}
