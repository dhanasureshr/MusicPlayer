package com.superpowered.playerexample;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.ViewHolder> {
    private ArrayList<PlaylistData> playlists;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public PlaylistAdapter(ArrayList<PlaylistData> playlists) {
        this.playlists = playlists;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
    @NonNull
    @Override
    public PlaylistAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.playlist_holder, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaylistAdapter.ViewHolder holder, int position) {
        holder.bind(playlists.get(position).playlist_name,playlists.get(position).playlist_total_items_count);

    }

    @Override
    public int getItemCount() {
        return playlists.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView PlayList_name;
        private TextView Contents_count;
        private ImageView PlayList_chooser;

        private ImageView PlayList_album_image;


        ViewHolder(@NonNull View itemView) {
            super(itemView);
            PlayList_name = itemView.findViewById(R.id.playlist_name);
            Contents_count = itemView.findViewById(R.id.contents_count);
            PlayList_chooser = itemView.findViewById(R.id.playlist_chooser);
            PlayList_album_image = itemView.findViewById(R.id.play_list_album_image);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getBindingAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }

        void bind(String playlist_name,String playList_total_itemcount) {
            PlayList_name.setText(playlist_name);
            Contents_count.setText(playList_total_itemcount);
        }

    }


}



